package gui.sgb;

import java.util.Date;
import java.util.List;

import gui.sgbmodel.entities.Adiantamento;
import gui.sgbmodel.entities.Funcionario;
import gui.sgbmodel.service.AdiantamentoService;
import gui.sgbmodel.service.FuncionarioService;

public class AdiantamentoUpdate {
	
	private static AdiantamentoService adiService = new AdiantamentoService();
	private static FuncionarioService funService = new FuncionarioService();
	private static Adiantamento adiantamento = new Adiantamento();
	private static Funcionario funcionario;
	private static Date data = new Date();
	
	// atualiza salario, se nao houver	
	public static void updateSemSalario(int mm, int aa) {
		List<Adiantamento> adDel = adiService.findMesTipo(mm, aa, "S");
		for (Adiantamento ad : adDel) {
			if (ad.getTipoAdi().contains("S")) {
				adiService.remove(ad.getNumeroAdi());
			}
		}
		int tam = funService.findByAtivo("Ativo", aa, mm).size();
		for (int i = 1 ; i < tam + 1 ; i++) {
			funcionario = funService.findById(i);
			if (!funcionario.getNomeFun().contains("Consumo Próprio")) {
				if (funcionario.getAnoFun() <= aa && funcionario.getMesFun() <= mm ) {
					adiantamento.setCodigoFun(funcionario.getCodigoFun());
					adiantamento.setNomeFun(funcionario.getNomeFun());
					adiantamento.setCargo(funcionario.getCargo());
					adiantamento.setSituacao(funcionario.getSituacao());
					
					adiantamento.setNumeroAdi(null);
					adiantamento.setDataAdi(data);
					adiantamento.setValeAdi(0.00);
					adiantamento.setMesAdi(mm);
					adiantamento.setAnoAdi(aa);
					adiantamento.setValorCartelaAdi(0.00);
					adiantamento.setCartelaAdi(0);
					adiantamento.setTipoAdi("S");
					adiantamento.setSalarioAdi(funcionario.getCargo().getSalarioCargo());
					adiantamento.setNomeAdi(funcionario.getNomeFun());
					adiantamento.setCargoAdi(funcionario.getCargoFun());
					adiantamento.setSituacaoAdi(funcionario.getSituacaoFun());
					adiantamento.getComissaoAdi();
					adiService.saveOrUpdate(adiantamento);
				}
			}
		}		
	}	
}