import { Routes } from '@angular/router';
import { BeneficioListComponent } from './components/beneficio-list/benefit-list.component';
import { BeneficioFormComponent } from './components/beneficio-form/benefit-form.component';
import { TransferComponent } from './components/transfer-dialog/transfer-form.component';

export const routes: Routes = [
  { path: '', redirectTo: '/beneficios', pathMatch: 'full' },
  { path: 'beneficios', component: BeneficioListComponent },
  { path: 'beneficios/novo', component: BeneficioFormComponent },
  { path: 'beneficios/editar/:id', component: BeneficioFormComponent },
  {
    path: 'beneficios/:id/transferir', component:  TransferComponent
  }
];
