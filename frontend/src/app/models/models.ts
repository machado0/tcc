export interface EstatisticasUsuario {
  nivel: number;
  xpAtual: number;
  xpProximoNivel: number;
  xpHoje: number;
  sequenciaDias: number;
  melhorSequenciaDias: number;
  moedas: number;
  vidas: number;
}

export interface Usuario {
  id: number;
  nome: string;
  email?: string;
  username?: string;
  avatarUrl?: string;
  tituloHonra?: string;
  dataCriacao?: string;
}

export interface Conquista {
  id: number;
  titulo: string;
  descricao: string;
  icone: string;
  desbloqueada: boolean;
  progressoAtual: number;
  meta: number;
  recompensaXp: number;
  categoria: 'GERAL' | 'ESTUDOS' | 'OFENSIVA' | 'MESTRE';
}

export interface MetricasDesempenho {
  questoesRespondidas: number;
  questoesCorretas: number;
  aulasConcluidas: number;
  tempoTotalEstudoMinutos: number;
  taxaAcerto: number;
}

export interface PerfilUsuario {
  usuario: Usuario;
  estatisticas: EstatisticasUsuario;
  metricas: MetricasDesempenho;
  conquistas: Conquista[];
}

export interface ContinuarEstudando {
  cursoId: number;
  cursoNome: string;
  moduloId: number;
  moduloNome: string;
  aulaId: number;
  aulaNome: string;
  progresso: number;
  ultimaDataEstudo: string;
}

export interface Missao {
  id: number;
  titulo: string;
  xp: number;
  concluida: boolean;
}

export type TipoMissao =
  | 'RESPONDER_QUESTOES'
  | 'ACERTAR_QUESTOES'
  | 'CONCLUIR_AULAS'
  | 'OBTER_XP';

export interface MissaoEstado extends Missao {
  tipo: TipoMissao;
  meta: number;
  progressoAtual: number;
  dataReferencia: string;
  dataConclusao?: string;
}

export interface ConclusaoAula {
  aulaId: number;
  moduloId?: number;
  acertos: number;
  total: number;
  xpTotal: number;
  dataConclusao: string;
}

export interface ProgressoDia {
  data: string;
  dia: string;
  xp: number;
  aulasConcluidas: number;
  questoesRespondidas: number;
}

export interface ProgressoSemanal {
  dias: ProgressoDia[];
  totalXp: number;
  diasEstudados: number;
}

export interface DadosHome {
  usuario: Usuario;
  estatisticas: EstatisticasUsuario;
  continuarEstudando: ContinuarEstudando | null;
  missoesDiarias: MissaoEstado[];
  progressoSemanal: ProgressoSemanal;
  metricas?: MetricasDesempenho;
  conquistas?: Conquista[];
}

export interface GamificacaoEstado {
  usuarioId: number;
  atualizadoEm: string;
  estatisticas: EstatisticasUsuario;
  metricas: MetricasDesempenho;
  missoes: MissaoEstado[];
  conquistas: Conquista[];
  progressoSemanal: ProgressoSemanal;
  conclusoes: ConclusaoAula[];
  rankingUsuario: AlunoRanking | null;
}

export interface ResultadoGamificacao {
  estado: GamificacaoEstado;
  conclusao: ConclusaoAula;
  missoesAtualizadas: MissaoEstado[];
  conquistasDesbloqueadas: Conquista[];
}

export type StatusItemEstudo = 'CONCLUIDA' | 'DISPONIVEL' | 'BLOQUEADA';

export interface AulaMapa {
  id: number;
  ordem: number;
  titulo: string;
  descricao?: string;
  duracaoMinutos: number;
  xpRecompensa: number;
  status: StatusItemEstudo;
}

export interface ModuloMapa {
  id: number;
  ordem: number;
  titulo: string;
  descricao: string;
  status: StatusItemEstudo;
  progressoPorcentagem: number;
  aulas: AulaMapa[];
}

export interface Materia {
  id: number;
  nome: string;
  descricao: string;
  icone: string;
  cor: string;
  progressoPorcentagem: number;
  totalAulas: number;
  aulasConcluidas: number;
  disponivel: boolean;
  modulos: ModuloMapa[];
}


export type TendenciaPosicao = 'SUBIU' | 'DESCEU' | 'MANTEVE';

export interface AlunoRanking {
  posicao: number;
  usuarioId: number;
  nome: string;
  username: string;
  avatarUrl?: string;
  nivel: number;
  xpSemana: number;
  sequenciaDias: number;
  tendencia: TendenciaPosicao;
  variacaoPosicoes?: number;
  ehUsuarioLogado: boolean;
}

export interface TurmaRanking {
  id: number;
  nomeTurma: string;
  periodo: string;
  terminaEmDias: number;
  totalAlunos: number;
  posicaoUsuarioLogado: number;
  alunos: AlunoRanking[];
}


export interface OpcaoQuestao {
  id: number;
  letra: 'A' | 'B' | 'C' | 'D';
  texto: string;
}

export interface QuestaoAula {
  id: number;
  ordem: number;
  enunciado: string;
  contextoEscolar?: string;
  opcoes: OpcaoQuestao[];
  opcaoCorretaId: number;
  explicacaoPedagogica: string;
  xpRecompensa: number;
}

export interface ConteudoTeorico {
  resumo: string;
  pontosChave: string[];
  dicaProfessor: string;
  exemploPratico: string;
}

export interface DetalheAula {
  id: number;
  moduloId: number;
  materiaNome: string;
  moduloNome: string;
  titulo: string;
  descricao: string;
  duracaoMinutos: number;
  xpTotal: number;
  conteudoTeorico: ConteudoTeorico;
  questoes: QuestaoAula[];
}

export interface ConfiguracoesUsuario {
  matricula: string;
  escola: string;
  turma: string;
  emailResponsaveis: string;
  tema: 'claro' | 'escuro' | 'sistema';
  tamanhoFonte: 'padrao' | 'grande';
  altoContraste: boolean;
  efeitosSonoros: boolean;
  animacoesGamificacao: boolean;
  notificarTarefas: boolean;
  notificarProvas: boolean;
  resumoSemanalPais: boolean;
}

export type TipoNotificacao = 'CONQUISTA' | 'MISSAO' | 'AULA' | 'SISTEMA';

export interface Notificacao {
  id: number;
  titulo: string;
  mensagem: string;
  tipo: TipoNotificacao;
  lida: boolean;
  criadaEm: string;
  rota?: string;
}
