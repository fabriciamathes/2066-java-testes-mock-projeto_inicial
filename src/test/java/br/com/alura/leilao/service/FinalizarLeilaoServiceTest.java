package br.com.alura.leilao.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

import br.com.alura.leilao.dao.LeilaoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;

class FinalizarLeilaoServiceTest {

	private FinalizarLeilaoService service;

	@Mock // @Mock: marca um atributo como sendo um mock
	private LeilaoDao leilaoDao;

	@Mock
	private EnviadorDeEmails enviadorDeEmails;

	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.initMocks(this);
		this.service = new FinalizarLeilaoService(leilaoDao, enviadorDeEmails);
	}

	@Test
	void deveriaFinalizarUmLeilao() {
		List<Leilao> leiloes = leiloes();

		// when e thenReturn: Altera o comportamento de um método do mock. Altera o
		// retorno padrão de um método no mock, configurado para utilizar os dados de
		// teste da classe "leiloes"
		Mockito.when(leilaoDao.buscarLeiloesExpirados()).thenReturn(leiloes);

		service.finalizarLeiloesExpirados();

		Leilao leilao = leiloes.get(0);
		Assert.assertTrue(leilao.isFechado());
		Assert.assertEquals(new BigDecimal("900"), leilao.getLanceVencedor().getValor());

		// verify: Checa se o mock teve um determinado método chamado, realizando
		// assert. Verificando se o método "salvar".
		Mockito.verify(leilaoDao).salvar(leilao);

	}

	@Test
	void deveriaEnviarUmEmailParaVencedorDoLeilao() {
		List<Leilao> leiloes = leiloes();

		// when e thenReturn: Altera o comportamento de um método do mock. Altera o
		// retorno padrão de um método no mock, configurado para utilizar os dados de
		// teste da classe "leiloes"
		Mockito.when(leilaoDao.buscarLeiloesExpirados()).thenReturn(leiloes);

		service.finalizarLeiloesExpirados();

		Leilao leilao = leiloes.get(0);
		Lance lanceVencedor = leilao.getLanceVencedor();

		// verify: Checa se o mock teve um determinado método chamado, realizando
		// assert. Verificando se o método "salvar".
		Mockito.verify(enviadorDeEmails).enviarEmailVencedorLeilao(lanceVencedor);

	}

	// dados de teste
	private List<Leilao> leiloes() {
		List<Leilao> lista = new ArrayList<>();

		Leilao leilao = new Leilao("Celular", new BigDecimal("500"), new Usuario("fulano"));

		Lance primeiro = new Lance(new Usuario("Beltrano"), new BigDecimal("600"));

		Lance segundo = new Lance(new Usuario("Ciclano"), new BigDecimal("900"));

		leilao.propoe(primeiro);
		leilao.propoe(segundo);

		lista.add(leilao);

		return lista;
	}

}
