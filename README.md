# Quiz sobre Geoparque Caçapava Mundial UNESCO com temática do Mario Kart
## Breno Rosa, Sistemas de Informação
### Running on GitHub Codespaces

1. Click on 'Code', select 'Codespaces' and click '+' to create a Codespace on master
2. In the terminal of the recently created Codespace:

   1. Update the Java version
   ```
   sdk install java 17.0.8-tem
   sdk default java 17.0.8-tem
   ```
   2. Go to the project folder
   ```
   cd gdx-1.13.0/a-simple-game
   ```
   3. Build the HTML project
   ```
   ./gradlew html:dist
   ```
   4. Run the HTML project
   ```
   cd html/build/dist
   python -m http.server
   ```
   
### Relatório:
O jogo surgiu da ideia de implementar um quiz já feito em outra disciplina (link para o quiz: https://quiz-app-gilt-one.vercel.app/) 
e o jogo Super Mario Kart no qual tenho boas memórias da minha infância.
Com as perguntas já selecionadas, foi clonado o projeto do simple-game e inicialmente criado e alterado os sprites para colocar o Mario. 
Após foi desenvolvido a movimentação do Mario, onde pode ser feito pelo mouse ou setas do teclado. Logo após foi a parte de exibir a pergunta, 
onde eu tive bastante dificuldade de inserir texto no jogo, então optei por criar um arraylist com imagens das perguntas (criadas no paint) e seus
respectivos sprites que representam as respostas. Como cada sprite representa uma resposta foi desenvolvido uma classe para eles serem gerados e 
percorrerem a tela, depois disso foi feita a parte de colisão. Após uma colisão, o jogo reconhece qual sprite foi colidido e troca a pergunta, 
onde as respostas certas tem um som e a errada tem outro, a resposta certa também soma +1 no placar.
O jogo finaliza depois de respondida a última questão apresentando o placar final. O jogo contém um pequeno problema com a vizualição do placar
e o resultado final, como dito anteriormente tive problemas para entender como funciona a inserção de texto no jogo.

![image](https://github.com/user-attachments/assets/f8f94dfc-c787-47f7-a251-3f2e4370c2f8)
