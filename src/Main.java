import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


/*
 * A implementa��o do programa � baseada em 6 fases:
 *  - Capturar entrada do usu�rio
 *  - Obter todas as poss�veis palavras partindo de uma sequencia de caracteres
 *  - Obter a pontua��o para cada palavra poss�vel
 *  - Obter a melhor pontua��o aplicando os crit�rios de desempatate quando necess�rio
 *  - Obter os caracteres que sobraram na forma��o de cada palavra
 *  - Imprimir resultado
 *   
 *  	Para obter todas a palavras formadas pela sequ�ncia foi utilizado um loop "for" iterando sobre lista wordDB, 
 *  	verificando para todas as palavras, exceto as maiores que a sequencia inserida pelo usu�rio, se � poss�vel a
 *  	forma��o partindo da sequ�ncia dada. Nos casos em que � poss�vel, a palavra � adicionada em uma lista que ser� retornada pela fun��o.
 *  
 *  	Para obter o score de todas as palavras formadas pela sequencia, foi utilizado um loop "for" iterando sobre a lista de palavras,
 *  	adicionando em uma lista, a pontua��o de cada uma delas.
 *  	O c�lculo do pontua��o � realizado percorrendo a palavra caracter � caracter somando seus valores a pontua��o da palavra. Caso o 
 *  	jogador tenha escolhido uma posi��o para duplicar a pontua��o, o bonus � adicionado atrav�s do m�todo 'calculateBonus' somando 
 *  	o valor do caracter na posi��o � pontua��o da palavra completa.
 *  
 *  	A melhor pontua��o � obtida percorrendo a lista de pontua��o e comparando uma a uma e alterando, quando necess�rio, as vari�veis com
 *  	a palavra com o maior score e outra com o score. A palavra e o score armazenado s�o retornados em uma vari�vel do tipo Map com as chaves
 *  	"word" e "score" para recuperar os dados quando necess�rio.
 *  
 *  	Para obter os caracteres que sobraram, um loop "for" percorrendo a palavra formada, caracter a caracter � executado,
 *  	removendo sua primeira ocorr�ncia na sequ�ncia inserida pelo usu�rio.
 *  	Ao final da itera��o, permanecem na sequ�ncia apenas os catacteres n�o utilizados para a forma��o das palavras.
 *  
 *  	Os valores obtidos acima s�o impressos no formato do exemplo de sa�da dado na especifica��o do desafio.
 *  
 *  	
 *  	
 * */
public class Main {
	
	// Banco de palavras
	private static List<String> wordsDB = Arrays.asList("Abacaxi", "Manada", "mandar", "porta", "mesa", "Dado", "Mangas", "J�", "coisas",
			"radiografia", "matem�tica", "Drogas", "pr�dios", "implementa��o", "computador", "bal�o",
			"X�cara", "T�dio", "faixa", "Livro", "deixar", "superior", "Profiss�o", "Reuni�o", "Pr�dios",
			"Montanha", "Bot�nica", "Banheiro", "Caixas", "Xingamento", "Infesta��o", "Cupim",
			"Premiada", "empanada", "Ratos", "Ru�do", "Antecedente", "Empresa", "Emiss�rio", "Folga",
			"Fratura", "Goiaba", "Gratuito", "H�drico", "Homem", "Jantar", "Jogos", "Montagem",
			"Manual", "Nuvem", "Neve", "Opera��o", "Ontem", "Pato", "P�", "viagem", "Queijo", "Quarto",
			"Quintal", "Solto", "rota", "Selva", "Tatuagem", "Tigre", "Uva", "�ltimo", "Vitup�rio",
			"Voltagem", "Zangado", "Zombaria", "Dor");
	
	// Armazena a posi��o b�nus informada pelo jogador
	private static Integer bonusPosition = 0;

	// Fun��o principal do programa
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		String userInput; 
		
		while(true) {
			// Captura os caracteres e posi��o do b�nus inseridos pelo jogador
			System.out.printf("Digite as letras dispon�veis nesta jogada: ");
			userInput = removerAcentos(scan.nextLine());
			System.out.printf("Digite a posi��o b�nus: ");
			try {
				bonusPosition = Integer.valueOf(scan.nextLine());
			}catch(Exception e){
				System.out.println("Nenhuma posi��o b�nus adicionada");
			}
			
			// Impress�o do resultado
			printResult(userInput);
			System.out.printf("\n");
			bonusPosition = 0;
			
		}

	}
	
	/*
	 * userInput - sequencia de caracteres inserida pelo jogador
	 * */
	public static void printResult(String userInput) {
		// Listas de poss�veis palavras e scores, map contendo palavra com melhor pontua��o e a pontua��o, vetor com caracteres n�o utilizados
		List<String> possibleWords = possibleWords(userInput);
		List<Integer> scores = getScores(possibleWords);
		Map<String, String> bestScore = bestScore(possibleWords, scores);
		char[] remainderCharsArr = getRemainderChars(userInput, bestScore.get("word"));

		//Verifica��o se existe palavra formada e impress�o do resultado
		if(possibleWords.size() == 0) {
			System.out.println("\nNenhuma palavra encontrada");
			
		}
		else {
			System.out.printf("\n%s, palavra de %s pontos\n", bestScore.get("word"), bestScore.get("score"));
			
		}
		
		// Itera��o sobre o vetor de caracteres n�o utilizados para impress�o no resultado
		System.out.printf("Sobraram: ");
		for(char c : remainderCharsArr) {			
			System.out.printf("%c, ", c);
		}
		System.out.printf("\n");
	}
	
	/*
	 * words - Lista de string contendo todas as palavras formadas
	 * */
	public static List<Integer> getScores(List<String> words) {
		// Adiciona a pontua��o de cada palavra � lista 'scores'. A lista 'scores' � retornada
		List<Integer> scores = new ArrayList<Integer>();
		
		for(String word : words) {
			scores.add(calculateScore(word));
		}
		
		return scores;
	}
	
	/*
	 * word - palavra que desejo calcular a pontua��o
	 * */
	public static Integer calculateScore(String word) {
		// Itera��o sobre a palavra somando o valor de cada caractere a vari�vel 'score'. O inteiro 'score' � retornado
		Integer score = 0;
		
		for(int i = 0; i < word.length(); i++){
			String character = Character.toString(word.charAt(i));
			
			if("AEIOUTNRLS".contains(character)) {
				score++;
			}
            else if( "DG".contains(character)){
                score += 2;
            }
            else if( "BCMP".contains(character)){
                score += 3;
            }
            else if( "FHV".contains(character)){
                score += 5;
            }
            else if( "JX".contains(character)){
                score += 8;
            }
            else if( "QZ".contains(character)){
                score += 13;
            }
            else{
            	continue;
            }
        }
		
		// Adiciona o b�nus a 'score'
		score = calculateBonus(word, score);
		
		return score;
	}
	
	/*
	 * word - palavra que desejo aplicar o b�nus de posi��o
	 * score - pontua��o da palavra, sem o b�nus adicionado
	 * */
	public static Integer calculateBonus(String word, Integer score) {
		// Se o jogador informou uma posi��o para aplicar o b�nus, o valor do caracter na posi��o � somado a 'score'. O inteiro 'score' � retornada
		if(bonusPosition > 0) {
			String character = Character.toString(word.charAt(bonusPosition-1));
			
			if("AEIOUTNRLS".contains(character)) {
				score++;
			}
            else if( "DG".contains(character)){
                score += 2;
            }
            else if( "BCMP".contains(character)){
                score += 3;
            }
            else if( "FHV".contains(character)){
                score += 5;
            }
            else if( "JX".contains(character)){
                score += 8;
            }
            else if( "QZ".contains(character)){
                score += 13;
            }
            else{
            	
            }
		}
		return score;
	}
	
	/*
	 * userInput - String com os caracteres informados pelo jogador
	 * */
	public static List<String> possibleWords(String userInput){
		// Itera��o sobre 'wordsDB' verificando se � poss�vel formar a palavra atrav�s de 'userInput', adicionando � lista 'acceptedWords' quando
		// for poss�vel a forma��o. A lista 'acceptedWords' � retornada
		List<String> acceptedWords = new ArrayList<String>();
		
		for(String word : wordsDB) {
			
			if(word.length() > userInput.length()) {
				continue;
			}
			
			word = removerAcentos(word);
			if(isPossible(userInput, word)) {
				acceptedWords.add(word);
			}
		}
		return acceptedWords;
	}
	
	/*
	 * sequence - String inserida pelo jogador
	 * word - Palavra qualquer
	 * */
	public static Boolean isPossible(String sequence, String word) {
		// Itera��o sobre os caracteres de 'sequence' removendo a primeira ocorrencia de 'sequence[i]' em 'word'
		Integer sequenceLen = sequence.length();
		
		for(int i = 0; i < sequenceLen; i++) {
			Integer index = word.indexOf(sequence.charAt(i));
			
			if(index != -1){
				word = word.replaceFirst(String.valueOf(sequence.charAt(i)), "");
			}
		}
		
		// Se 'wordLen = 0' � poss�vel a forma��o de 'word' atrav�s de 'sequence' ent�o retorna true se n�o, retorna false
		Integer wordLen = word.length();
		if( wordLen == 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/*
	 * words - Lista com todas as poss�veis palavras formadas partindo de 'userInput'
	 * scores - Lista com a pontua��o de cada palavra em 'words'
	 * */
	public static Map<String, String> bestScore(List<String> words, List<Integer> scores) {
		
		Integer biggestScore = 0, index = 0;
		String word = new String();
		
		// Itera��o sobre a lista 'scores' comparando cada 'score' e atualizando 'biggestScore' quando necess�rio
		for(Integer score : scores) {
			// Palavra de 'wodrs' que corresponde a 'score'
			String currentWord = words.get(index);
			
			// Se 'score' � maior que 'biggestScore' substitui 'word' e 'biggestSocre' pelos correspondentes a 'score'
			if(score > biggestScore) {
				biggestScore = score;
				word = currentWord;
			}
			// Se forem iguais
			else if(score == biggestScore) {
				// Verifica qual palavra tem o maior tamanho e substitui 'word' e 'biggestSocre' pelos correspondentes � menor palavra
				if(currentWord.length() < word.length()) {
					word = currentWord;
					biggestScore = score;
				}
				// Se os tamanhos forem iguais
				else if(currentWord.length() == word.length()) {
					// Ordena em ordem alfab�tica e substitui 'word' e 'biggestSocre' pelos correspondentes a primeira palavra da ordem
					String[] array = {currentWord, word};
					Arrays.sort(array);
					if(array[0].equals(currentWord)) {
						word = currentWord;
						biggestScore = score;
					}
				}
			}
			index++;
		}
		
		// Um map 'result' recebe a palavra com maior pontua��o e seu score com as chaves "word" e "score" respectivamente
		Map<String, String> result = new HashMap<>();
		result.put("word", word);
		result.put("score", Integer.toString(biggestScore));
		// retorna result
		return result;
	}
	
	/*
	 * userInput - sequencia de caracteres inserida pelo jogador
	 * word - palavra qualquer
	 * */
	public static char[] getRemainderChars(String userInput, String word) {
		Integer wordLen = word.length();
		
		// Itera��o sobre 'word' caracter a caracter, removendo a primeira ocorr�ncia de 'word[i]' em 'userInput', restando os caracteres
		// n�o utilizados
		for(int i = 0; i < wordLen; i++) {
			Integer index = userInput.indexOf(word.charAt(i));
			
			if(index != -1){
				userInput = userInput.replaceFirst(String.valueOf(word.charAt(i)), "");
			}
		}
		
		// Retorna userInput em um array de caracteres
		return userInput.toCharArray();
	}
	
	/*
	 * letras - String de caracteres inserida pelo jogador
	 * */
	public static String removerAcentos(String letras) {

        if(letras == null){
            return null;
        }
        else{
        	// Retorna a string letras em caixa auta, sem espa�os e caracteres e acentos
        	letras = letras.replaceAll(" ","");
            return Normalizer.normalize(letras, Normalizer.Form.NFD)
                    .replaceAll("\\p{InCOMBINING_DIACRITICAL_MARKS}+", "").toUpperCase();
        }
    }

}
