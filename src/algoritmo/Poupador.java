package algoritmo;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import controle.Constantes;

public class Poupador extends ProgramaPoupador {
	
	// Tipo de célula
    private int SEM_VISAO = -2;
    private int FORA_DO_AMBIENTE = -1;
    private int CELULA_VAZIA = 0;
    private int PAREDE = 1;
    private int BANCO = 3;
    private int MOEDA = 4;
    private int PASTILHA = 5;
    private int LADRAO = 200;

    // Ação do poupador
    private int FICAR_PARADO = 0;
    private int MOVER_CIMA = 1;
    private int MOVER_BAIXO = 2;
    private int MOVER_DIREITA = 3;
    private int MOVER_ESQUERDA = 4;
	
    // Define o tamanho do mapa
    private int TAMANHO_MAPA = 32;
    private int[][] mapa;
    
    private int posX, posY;
    
    Constantes teste = new Constantes();
	
	public int acao() {
		Point posicaoAtual = sensor.getPosicao();
	    int[] visao = sensor.getVisaoIdentificacao();
	    posX = (int) posicaoAtual.getX();
        posY = (int) posicaoAtual.getY();
        
        int melhorMovimento = 0;
        int melhorValor = 0;
    
	    
	    if(mapa == null)
	    {
	    	mapa = new int[TAMANHO_MAPA][TAMANHO_MAPA];
	    	this.inicializandoMapa();
	    }
	    
//		this.processarVisao(visao, posicaoAtual);
        
        List<Integer> valoresCaminhos = new ArrayList<>();
        
		// Loop pelos possíveis movimentos
        for (int i = 1; i <= 4; i++) {
			
        	Point proximaPosicao = calcularProximaPosicao(posicaoAtual, i);
        	
			// Verifica se a próxima posição é uma extremidade, uma parede ou está fora do mapa
			if (this.verificarExtremidadesMapa(posicaoAtual, i)) {
				
				// Avalia o valor da próxima posição
				int valorVisao = avaliarPosicao(visao, proximaPosicao);
				
				// Verifica se a próxima posição é melhor que a posição atual
                if (valorVisao > melhorValor) {
                    melhorValor = valorVisao;
                    melhorMovimento = i;
                }
                else
                {
                	melhorMovimento = (int) (Math.random() * 5);
                }
                
                while(melhorMovimento == 0)
            	{
            		melhorMovimento = (int) (Math.random() * 5);
            	}
			}	
        }

        return melhorMovimento;
	}
	
	public boolean verificarExtremidadesMapa(Point posicaoAtual, int i) {
		if(
    			!((posicaoAtual.y == 0 && i == MOVER_CIMA))
    			&& !((posicaoAtual.y == 29 && i == MOVER_BAIXO))
    			&& !((posicaoAtual.x == 29 && i == MOVER_DIREITA))
    			&& !((posicaoAtual.x == 0 && i == MOVER_ESQUERDA))
    	  )
		{
			return true;
		}
		return false;
	}
	
	public void inicializandoMapa() {
		for (int i = 0; i < TAMANHO_MAPA; i++) {
		    for (int j = 0; j < TAMANHO_MAPA; j++) {
		    	mapa[i][j] = SEM_VISAO;
		    }
		}
	}
	
	// método que recebe a visão do agente e adiciona na matriz;
	public void processarVisao(int[] visao, Point posicao) {
		int posicaoXAgente = (int) posicao.getX();
		int posicaoYAgente = (int) posicao.getY();
		int xMatriz = 0;
		int yMatriz = 0;
	    int i = 0;
	    for (int y = 0; y < 5; y++) {
	        for (int x = 0; x < 5; x++) {
	        	if(i < visao.length)
	        	{
	        		int valor = visao[i];
	        		xMatriz = posicaoXAgente + x - 2;
	        		yMatriz = posicaoYAgente + y - 2;
	        		if (xMatriz >= 0 && xMatriz < TAMANHO_MAPA && yMatriz >= 0 && yMatriz < TAMANHO_MAPA
	        				&& xMatriz != posicaoXAgente && yMatriz != posicaoYAgente) {
	        			this.mapa[xMatriz][yMatriz] = valor;
	        		}	        		
	        	}
	        	if(xMatriz != posicaoXAgente && yMatriz != posicaoYAgente)
	        	{
	        		i++;	        		
	        	}
	        }
	    }
	}
    
    private Point calcularProximaPosicao(Point posicaoAtual, int movimento) {

        Point proximaPosicao = new Point(posicaoAtual);

		    switch (movimento) {
		        case 1: // mover_cima
		        	if(!(proximaPosicao.y == 0))
        			{
		        		proximaPosicao.translate(0, -1);            		        		
        			}
		            break;
		        case 2: // mover_baixo
		        	if(!(proximaPosicao.y == 29))
        			{
		        		proximaPosicao.translate(0, 1);        		
        			}
		            break;
		        case 3: // mover_direita
		        	if(!(proximaPosicao.x == 29))
		        	{
		        		proximaPosicao.translate(1, 0);		        		
		        	}
		            break;
		        case 4: // mover_esquerda
		        	if(!(proximaPosicao.x == 0))
		        	{
		        		proximaPosicao.translate(-1, 0);		        		
		        	}
		            break;
		    }

        return proximaPosicao;
    }
    
    private int avaliarPosicao(int[] visao, Point posicao) {

        int valor = 0;
        
//        if(mapa[posicao.x][posicao.y] == MOEDA)
//		{
//			valor += 20;
//		}
//		
//		// Verifica se a posição tem um banco
//		if (mapa[posicao.x][posicao.y] == BANCO) {
//			valor += 40;
//		}
//		
//		// Verifica se a posição de exploração
//		if (mapa[posicao.x][posicao.y] == SEM_VISAO) {
//			valor += 10;
//		}
//		
//		// Verifica se a posição de exploração
//		if (mapa[posicao.x][posicao.y] == PASTILHA) {
//			valor += 15;
//		}
//				
//		// Verifica se a posição tem celula vazia
//		if (mapa[posicao.x][posicao.y] == CELULA_VAZIA) {
//			valor += 1;
//		}
//		
//		// Verifica se a posição tem celula vazia
//		if (mapa[posicao.x][posicao.y] == PAREDE) {
//			valor -= 10;
//		}	
//		
//		// Verifica se a posição tem ladrão
//		if (mapa[posicao.x][posicao.y] >= LADRAO) {
//			valor -= 70;
//		}
		
		// Verifica se a posição tem o ladrão na visão do poupador
        for (int i = 0; i < visao.length; i++) {
            if (visao[i] >= LADRAO) { 
                valor -= 100;
            }
            
            if (visao[i] == MOEDA) { 
                valor += 20;
            }
            
            if (visao[i] == BANCO) { 
                valor += 40;
            }
            
            if (visao[i] == PASTILHA) { 
            	valor += 15;
            }
            
            if(visao[i] == CELULA_VAZIA)
            {
            	valor += 1;
            }
            
            if(visao[i] == PAREDE)
            {
            	valor -= 10;
            }
        }

        return valor;
    }
    
}