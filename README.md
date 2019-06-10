Reconstrução do clássico Bomberman em arquitetura cliente-servidor para a disciplina Técnicas de Programação.

# Como executar
No diretório bomber-man/src:
- Compile: `javac *.java`
- Inicie o servidor: `java Server`
- Inicie o cliente: `java Client`

# Introdução
Lançado pela desenvolvedora Hudson Soft em 1983, o clássico Bomberman foi um dos jogos mais conhecidos da história.
Trata-se de um jogo de estratégia onde vence aquele que explodir o adversário, desde que não tenha se matado antes. Para isso é preciso destruir as paredes não-fixas com bombas explosivas para abrir caminho e, também, contar com a sorte para tentar encontrar, de forma aleatória, itens que dão um upgrade no personagem, como uma melhora do alcance da bomba ou um aumento da quantidade máxima de bombas simultâneas.

# Objetivo
Desenvolver programas em Java para que no mínimo duas pessoas, possivelmente localizadas remotamente, possam jogar interagindo através do teclado em um mapa 2D exibido para o usuário através de uma interface gráfica.

# Materiais e métodos
O jogo apresenta uma arquitetura cliente-servidor onde dois jogadores remotos, interagindo com programas clientes idênticos, disputam através de um programa servidor também remoto. Estes programas serão desenvolvidos utilizando a linguagem Java, e Swing para o desenvolvimento da interface. O programa cliente será responsável pela interface com o usuário.
Através de um mapa exibido por uma interface gráfica, o jogador fará seus movimentos com as setas do teclado para movimentar o seu personagem para cima, baixo, direita ou esquerda, e ao apertar a tecla B a bomba será plantada na posição atual. O servidor é responsável por intermediar os programas clientes. Este programa fica esperando as jogadas realizadas pelos clientes. Quando uma jogada é recebida, este programa servidor fará a consistência da jogada, atualizará uma representação interna do jogo e devolverá a todos os clientes o estado atual do mapa, se, por outro lado, for tentada em uma jogada inválida, este programa ignorará a jogada.

# Spriters
   - zanaku
   - pico 8 tiles: https://kicked-in-teeth.itch.io/pico-8-tiles (se realmente usar)