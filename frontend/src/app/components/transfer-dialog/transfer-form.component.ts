import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';

import { BeneficioService } from '../../services/benefit.service';
import { TransferRequest } from '../../models/transfer-request.model';

@Component({
  selector: 'app-transfer',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './transfer-form.component.html',
  styleUrls: ['./transfer-form.component.scss']
})
export class TransferComponent implements OnInit {
  beneficioOrigem: any = null;
  form!: FormGroup;
  submitting = false;

  constructor(
    private fb: FormBuilder,
    private snackBar: MatSnackBar,
    private beneficioService: BeneficioService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const state = history.state as { beneficioOrigem?: any };

    if (state?.beneficioOrigem) {
      this.beneficioOrigem = state.beneficioOrigem;
      console.log('Benefício carregado com sucesso:', this.beneficioOrigem);
    } else {
      this.tentarCarregarPeloId();
      return;
    }

    this.iniciarFormulario();
  }

  private tentarCarregarPeloId(): void {
    const urlSegments = this.router.url.split('/');
    const idIndex = urlSegments.indexOf('beneficios') + 1;
    const id = urlSegments[idIndex] ? Number(urlSegments[idIndex]) : null;

    if (!id || isNaN(id)) {
      this.voltarComErro();
      return;
    }

    this.beneficioService.findById(id).subscribe({
      next: (beneficio) => {
        this.beneficioOrigem = beneficio;
        console.log('Benefício carregado via API (fallback):', beneficio);
        this.iniciarFormulario();
      },
      error: () => {
        this.voltarComErro();
      }
    });
  }

  private iniciarFormulario(): void {
    this.form = this.fb.group({
      destinoId: [null, [Validators.required, Validators.min(1)]],
      valor: [null, [Validators.required, Validators.min(0.01)]]
    });

    this.form.get('valor')?.valueChanges.subscribe(() => {
      this.validarSaldoInsuficiente();
    });

    this.validarSaldoInsuficiente();
  }

  private validarSaldoInsuficiente(): void {
    const valorControl = this.form.get('valor');
    if (!valorControl || !this.beneficioOrigem) return;

    const saldoDisponivel = this.beneficioOrigem.value || 0;
    const valorInformado = Number(valorControl.value) || 0;

    if (valorInformado > saldoDisponivel && valorInformado > 0) {
      valorControl.setErrors({ ...valorControl.errors, saldoInsuficiente: true });
    } else {
      const errors = valorControl.errors || {};
      if (errors['saldoInsuficiente']) {
        delete errors['saldoInsuficiente'];
        valorControl.setErrors(Object.keys(errors).length ? errors : null);
      }
    }
  }

  private voltarComErro(): void {
    this.snackBar.open('Benefício não encontrado.', 'Fechar', { duration: 5000 });
    this.router.navigate(['/beneficios']);
  }

  confirmarTransferencia(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.snackBar.open('Corrija os erros no formulário.', 'Fechar', { duration: 4000 });
      return;
    }

    const origemId = this.beneficioOrigem.id;
    const destinoId = Number(this.form.value.destinoId);
    const valorTransferir = Number(this.form.value.valor);

    if (destinoId === origemId) {
      this.snackBar.open('Não é permitido transferir para o mesmo benefício.', 'Fechar', { duration: 4000 });
      return;
    }

    this.submitting = true;

    const request: TransferRequest = {
      benefitOriginId: origemId,
      benefitDestinationId: destinoId,
      value: valorTransferir
    };

    this.beneficioService.transferir(request).subscribe({
      next: () => {
        this.snackBar.open('Transferência realizada com sucesso!', 'Fechar', {
          duration: 5000,
          panelClass: ['success-snackbar']
        });
        this.router.navigate(['/beneficios']);
      },
      error: (err) => {
        this.submitting = false;
        const mensagem = err.error?.message || err.error?.error || 'Erro ao realizar a transferência.';
        this.snackBar.open(mensagem, 'Fechar', {
          duration: 6000,
          panelClass: ['error-snackbar']
        });
      }
    });
  }

  cancelar(): void {
    this.router.navigate(['/beneficios']);
  }
}