import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BeneficioService } from '../../services/benefit.service';

@Component({
  selector: 'app-beneficio-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatCheckboxModule,
    MatSnackBarModule
  ],
  templateUrl: './benefit-form.component.html',
  styleUrls: ['./benefit-form.component.scss']
})
export class BeneficioFormComponent implements OnInit {
  form!: FormGroup;
  isEditMode = false;
  beneficioId?: number;
  submitting = false;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private route: ActivatedRoute,
    private beneficioService: BeneficioService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
      description: ['', [Validators.maxLength(500)]],
      value: [0, [Validators.required, Validators.min(0)]],
      active: [true]
    });

    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.beneficioId = +id;
      this.loadBenefit();
    }
  }

  loadBenefit(): void {
    this.beneficioService.findById(this.beneficioId!).subscribe((b) => {
      this.form.patchValue(b);
    });
  }

  save(): void {
    if (this.form.invalid) return;

    this.submitting = true;
    const benefit = this.form.value;

    const obs = this.isEditMode
      ? this.beneficioService.update(this.beneficioId!, benefit)
      : this.beneficioService.create(benefit);

    obs.subscribe({
      next: () => {
        this.snackBar.open('Benefício salvo com sucesso!', 'Fechar', { duration: 3000 });
        this.router.navigate(['/beneficios']);
      },
      error: () => (this.submitting = false)
    });
  }

  cancel(): void {
    this.router.navigate(['/beneficios']);
  }
}
