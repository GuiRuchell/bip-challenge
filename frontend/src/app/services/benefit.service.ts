import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Beneficio } from '../models/benefit.model';
import { TransferRequest } from '../models/transfer-request.model';
import { TransferResponse } from '../models/transfer-response.model';

@Injectable({ providedIn: 'root' })
export class BeneficioService {
  private api = '/api/beneficios';

  constructor(private http: HttpClient) {}

  findAll(): Observable<Beneficio[]> {
    return this.http.get<Beneficio[]>(this.api);
  }

  findById(id: number): Observable<Beneficio> {
    return this.http.get<Beneficio>(`${this.api}/${id}`);
  }

  create(beneficio: Beneficio): Observable<Beneficio> {
    return this.http.post<Beneficio>(this.api, beneficio);
  }

  update(id: number, beneficio: Beneficio): Observable<Beneficio> {
    return this.http.put<Beneficio>(`${this.api}/${id}`, beneficio);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/${id}`);
  }

  transferir(request: TransferRequest): Observable<TransferResponse> {
    return this.http.post<TransferResponse>(`${this.api}/transferir`, request);
  }
}