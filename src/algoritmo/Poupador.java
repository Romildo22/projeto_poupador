package algoritmo;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Poupador extends ProgramaPoupador {
	
	// Tipo de célula
    private static final int SEM_VISAO = -2;
    private static final int FORA_DO_AMBIENTE = -1;
    private static final int CELULA_VAZIA = 0;
    private static final int PAREDE = 1;
    private static final int BANCO = 3;
    private static final int MOEDA = 4;
    private static final int PASTILHA = 5;

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
//	    int direcao = FICAR_PARADO;
	    posX = (int) posicaoAtual.getX();
        posY = (int) posicaoAtual.getY();

        int proxMovimento = 0;
        int distanciaMinima = Integer.MAX_VALUE;
	    
	    if(mapa == null)
	    {
	    	mapa = new int[TAMANHO_MAPA][TAMANHO_MAPA];
	    	this.inicializandoMapa();
	    }
	    if (moedas == null) {
            moedas = new int[2];
        }
	    
		this.processarVisao(visao, posicaoAtual);
//		this.movimentarAgente(visao, posicaoAtual);
			
//		return (int) (Math.random() * 5);
//		return proxMovimento;
		
		// Procura a moeda mais próxima
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (mapa[i][j] == 4) {
                    int distancia = calculaDistancia(posX, posY, i, j);
                    if (distancia < distanciaMinima) {
                        distanciaMinima = distancia;
                        proxMovimento = direcaoMovimento(posX, posY, i, j);
                    }
                }
                else
                {
                	proxMovimento = (int) (Math.random() * 5);
                }
            }
        }

        return proxMovimento;
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
	
	private int calculaDistancia(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    private int direcaoMovimento(int x1, int y1, int x2, int y2) {
        if (x1 == x2 && y1 == y2) {
            return 0;
        } else if (x1 == x2 && y1 > y2) {
            return 1;
        } else if (x1 == x2 && y1 < y2) {
            return 2;
        } else if (y1 == y2 && x1 < x2) {
            return 3;
        } else if (y1 == y2 && x1 > x2) {
            return 4;
        }
        return (int) (Math.random() * 5);
    }

}