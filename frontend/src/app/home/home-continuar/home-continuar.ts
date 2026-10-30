import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ContinuarEstudando } from '../../models/models';

@Component({
  selector: 'app-home-continuar',
  imports: [RouterLink],
  templateUrl: './home-continuar.html',
})
export class CardContinuar {
  continuarEstudando = input<ContinuarEstudando | null>(null);

  getUltimaMateria(): string {
    return this.continuarEstudando()?.cursoNome ?? '';
  }

  getUltimaAula(): string {
    return this.continuarEstudando()?.aulaNome ?? 'Nenhuma aula em andamento';
  }

  getProgresso(): number {
    return this.continuarEstudando()?.progresso ?? 0;
  }

  getDataUltimoAcesso(): Date | null {
    const data = this.continuarEstudando()?.ultimaDataEstudo;
    return data ? new Date(data) : null;
  }

  getDiferencaDias(data: Date | null): number {
    if (!data) {
      return 0;
    }

    const diferenca = new Date().getTime() - data.getTime();
    return Math.max(0, Math.floor(diferenca / (24 * 60 * 60 * 1000)));
  }

  getUltimoAcessoFormatado(): string {
    if (!this.continuarEstudando()) {
      return 'Ainda não iniciado';
    }

    const dias = this.getDiferencaDias(this.getDataUltimoAcesso());

    if (dias <= 1) {
      return 'hoje';
    }

    if (dias <= 7) {
      return `há ${dias} dias`;
    }

    const semanas = Math.floor(dias / 7);
    return `há ${semanas} semana${semanas === 1 ? '' : 's'}`;
  }
}
