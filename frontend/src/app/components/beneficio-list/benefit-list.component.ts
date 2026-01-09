import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router'; // já tem
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { BeneficioService } from '../../services/benefit.service';
import { Beneficio } from '../../models/benefit.model';

@Component({
  selector: 'app-beneficio-list',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './benefit-list.component.html',
  styleUrls: ['./benefit-list.component.scss']
})
export class BeneficioListComponent implements OnInit {
  benefits: Beneficio[] = [];
  loading = false;

  constructor(
    private beneficioService: BeneficioService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadBenefits();
  }

  loadBenefits(): void {
    this.loading = true;
    this.beneficioService.findAll().subscribe({
      next: (res) => {
        this.benefits = res;
        this.loading = false;
      },
      error: () => (this.loading = false)
    });
  }

  newBenefit(): void {
    this.router.navigate(['/beneficios/novo']);
  }

  editBenefit(b: Beneficio): void {
    this.router.navigate(['/beneficios/editar', b.id]);
  }

  deleteBenefit(b: Beneficio): void {
    if (!b.id) return;
    if (confirm(`Deseja realmente desativar o benefício "${b.name}"?`)) {
      this.beneficioService.delete(b.id).subscribe(() => this.loadBenefits());
    }
  }

  transferBenefit(b: Beneficio) {
    this.router.navigate(['/beneficios', b.id, 'transferir'], {
      state: { beneficioOrigem: b } 
    });
  }
}