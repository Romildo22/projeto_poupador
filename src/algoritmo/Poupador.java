package algoritmo;

import java.awt.Point;

public class Poupador extends ProgramaPoupador {
	
	// Tipo de célula
    private static final int SEM_VISAO = -2;
    private static final int FORA_DO_AMBIENTE = -1;
    private static final int CELULA_VAZIA = 0;
    private static final int PAREDE = 1;
    private static final int BANCO = 3;
    private static final int MOEDA = 4;
    private static final int PASTILHA = 5;
    private static final int LADRAO = 200;

    // Ação do poupador
    private static final int FICAR_PARADO = 0;
    private static final int MOVER_CIMA = 1;
    private static final int MOVER_BAIXO = 2;
    private static final int MOVER_DIREITA = 3;
    private static final int MOVER_ESQUERDA = 4;
	
    // Define o tamanho do mapa
    private static final int TAMANHO_MAPA = 30;
    private int[][] mapa;
    private int[] moedas;
    private int posX, posY;
	
	public int acao() {
		Point posicaoAtual = sensor.getPosicao();
	    int[] visao = sensor.getVisaoIdentificacao();
	    posX = (int) posicaoAtual.getX();
        posY = (int) posicaoAtual.getY();
        
        int melhorMovimento = 0;
        int melhorValor = Integer.MIN_VALUE;
	    
	    if(mapa == null)
	    {
	    	mapa = new int[TAMANHO_MAPA][TAMANHO_MAPA];
	    	this.inicializandoMapa();
	    }
	    if (moedas == null) {
            moedas = new int[2];
        }
	    
		this.processarVisao(visao, posicaoAtual);
        
     // Loop pelos possíveis movimentos
        for (int i = 1; i <= 4; i++) {

        	if(
        			!(posicaoAtual.y == 0 && i == MOVER_CIMA)
        			&& !(posicaoAtual.y == 29 && i == MOVER_BAIXO)
        			&& !(posicaoAtual.x == 0 && i == MOVER_ESQUERDA)
        			&& !(posicaoAtual.x == 29 && i == MOVER_DIREITA)
        	  )
        	{
        		Point proximaPosicao = calcularProximaPosicao(posicaoAtual, i);
        		
        		// Verifica se a próxima posição é uma parede ou está fora do mapa
        		if (mapa[proximaPosicao.x][proximaPosicao.y] != PAREDE && mapa[proximaPosicao.x][proximaPosicao.y] != FORA_DO_AMBIENTE) {
        			
        			// Avalia o valor da próxima posição
        			int valor = avaliarPosicao(mapa, visao, proximaPosicao);
        			
        			// Verifica se a próxima posição é melhor que a posição atual
        			if (valor > melhorValor) {
        				melhorValor = valor;
        				melhorMovimento = i;
        			}
        		}	
        	}
        }

        return melhorMovimento;
	}
	
	public void inicializandoMapa() {
		for (int i = 0; i < 30; i++) {
		    for (int j = 0; j < 30; j++) {
		    	mapa[i][j] = SEM_VISAO;
		    }
		}
	}
	
	// método que recebe a visão do agente e adiciona na matriz;
	public void processarVisao(int[] visao, Point posicao) {
		int posicaoXAgente = (int) posicao.getX();
		int posicaoYAgente = (int) posicao.getY();
	    int i = 0;
	    for (int y = 0; y < 5; y++) {
	        for (int x = 0; x < 5; x++) {
	        	if(i < visao.length)
	        	{
	        		int valor = visao[i];
	        		int xMatriz = posicaoXAgente + x - 2;
	        		int yMatriz = posicaoYAgente + y - 2;
	        		if (xMatriz >= 0 && xMatriz < 30 && yMatriz >= 0 && yMatriz < 30) {
	        			this.mapa[xMatriz][yMatriz] = valor;
	        		}	        		
	        	}
	            i++;
	        }
	    }
	}
	
	private boolean encontrouMoeda(int[] visao) {
        for (int i = 0; i < visao.length; i++) {
            if (visao[i] == 4) {
                return true;
            }
        }
        return false;
    }
    
    private Point calcularProximaPosicao(Point posicaoAtual, int movimento) {

        Point proximaPosicao = new Point(posicaoAtual);

        switch (movimento) {
            case 1: // mover_cima
                proximaPosicao.translate(0, -1);
                break;
            case 2: // mover_baixo
                proximaPosicao.translate(0, 1);
                break;
            case 3: // mover_direita
                proximaPosicao.translate(1, 0);
                break;
            case 4: // mover_esquerda
                proximaPosicao.translate(-1, 0);
                break;
        }

        return proximaPosicao;
    }
    
    private int avaliarPosicao(int[][] mapa, int[] visao, Point posicao) {

        int valor = 0;

        // Verifica se a posição tem uma moeda ou banco
        if (mapa[posicao.x][posicao.y] == MOEDA || mapa[posicao.x][posicao.y] == BANCO) {
            valor += 10;
        }

        // Verifica se a posição tem o ladrão
        for (int i = 0; i < visao.length; i++) {
            if (visao[i] >= LADRAO) { // O ladrão é representado pelo número 6
                valor -= 100;
                break;
            }
        }

        // Verifica se a posição é uma parede
        if (mapa[posicao.x][posicao.y] == 1) {
            valor -= 5;
        }

        return valor;
    }

}