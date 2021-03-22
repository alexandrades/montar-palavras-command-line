import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


/*
 * A implementação do programa é baseada em 6 fases:
 *  - Capturar entrada do usuário
 *  - Obter todas as possíveis palavras partindo de uma sequencia de caracteres
 *  - Obter a pontuação para cada palavra possível
 *  - Obter a melhor pontuação aplicando os critérios de desempatate quando necessário
 *  - Obter os caracteres que sobraram na formação de cada palavra
 *  - Imprimir resultado
 *   
 *  	Para obter todas a palavras formadas pela sequência foi utilizado um loop "for" iterando sobre lista wordDB, 
 *  	verificando para todas as palavras, exceto as maiores que a sequencia inserida pelo usuário, se é possível a
 *  	formação partindo da sequência dada. Nos casos em que é possível, a palavra é adicionada em uma lista que será retornada pela função.
 *  
 *  	Para obter o score de todas as palavras formadas pela sequencia, foi utilizado um loop "for" iterando sobre a lista de palavras,
 *  	adicionando em uma lista, a pontuação de cada uma delas.
 *  	O cálculo do pontuação é realizado percorrendo a palavra caracter à caracter somando seus valores a pontuação da palavra. Caso o 
 *  	jogador tenha escolhido uma posição para duplicar a pontuação, o bonus é adicionado através do método 'calculateBonus' somando 
 *  	o valor do caracter na posição à pontuação da palavra completa.
 *  
 *  	A melhor pontuação é obtida percorrendo a lista de pontuação e comparando uma a uma e alterando, quando necessário, as variáveis com
 *  	a palavra com o maior score e outra com o score. A palavra e o score armazenado são retornados em uma variável do tipo Map com as chaves
 *  	"word" e "score" para recuperar os dados quando necessário.
 *  
 *  	Para obter os caracteres que sobraram, um loop "for" percorrendo a palavra formada, caracter a caracter é executado,
 *  	removendo sua primeira ocorrência na sequência inserida pelo usuário.
 *  	Ao final da iteração, permanecem na sequência apenas os catacteres não utilizados para a formação das palavras.
 *  
 *  	Os valores obtidos acima são impressos no formato do exemplo de saída dado na especificação do desafio.
 *  
 *  	
 *  	
 * */
public class Main {
	
	// Banco de palavras
	private static List<String> wordsDB = Arrays.asList("Abacaxi", "Manada", "mandar", "porta", "mesa", "Dado", "Mangas", "Já", "coisas",
			"radiografia", "matemática", "Drogas", "prédios", "implementação", "computador", "balão",
			"Xícara", "Tédio", "faixa", "Livro", "deixar", "superior", "Profissão", "Reunião", "Prédios",
			"Montanha", "Botânica", "Banheiro", "Caixas", "Xingamento", "Infestação", "Cupim",
			"Premiada", "empanada", "Ratos", "Ruído", "Antecedente", "Empresa", "Emissário", "Folga",
			"Fratura", "Goiaba", "Gratuito", "Hídrico", "Homem", "Jantar", "Jogos", "Montagem",
			"Manual", "Nuvem", "Neve", "Operação", "Ontem", "Pato", "Pé", "viagem", "Queijo", "Quarto",
			"Quintal", "Solto", "rota", "Selva", "Tatuagem", "Tigre", "Uva", "Último", "Vitupério",
			"Voltagem", "Zangado", "Zombaria", "Dor");
	
	// Armazena a posição bônus informada pelo jogador
	private static Integer bonusPosition = 0;

	// Função principal do programa
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		String userInput; 
		
		while(true) {
			// Captura os caracteres e posição do bônus inseridos pelo jogador
			System.out.printf("Digite as letras disponíveis nesta jogada: ");
			userInput = removerAcentos(scan.nextLine());
			System.out.printf("Digite a posição bônus: ");
			try {
				bonusPosition = Integer.valueOf(scan.nextLine());
			}catch(Exception e){
				System.out.println("Nenhuma posição bônus adicionada");
			}
			
			// Impressão do resultado
			printResult(userInput);
			System.out.printf("\n");
			bonusPosition = 0;
			
		}

	}
	
	/*
	 * userInput - sequencia de caracteres inserida pelo jogador
	 * */
	public static void printResult(String userInput) {
		// Listas de possíveis palavras e scores, map contendo palavra com melhor pontuação e a pontuação, vetor com caracteres não utilizados
		List<String> possibleWords = possibleWords(userInput);
		List<Integer> scores = getScores(possibleWords);
		Map<String, String> bestScore = bestScore(possibleWords, scores);
		char[] remainderCharsArr = getRemainderChars(userInput, bestScore.get("word"));

		//Verificação se existe palavra formada e impressão do resultado
		if(possibleWords.size() == 0) {
			System.out.println("\nNenhuma palavra encontrada");
			
		}
		else {
			System.out.printf("\n%s, palavra de %s pontos\n", bestScore.get("word"), bestScore.get("score"));
			
		}
		
		// Iteração sobre o vetor de caracteres não utilizados para impressão no resultado
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
		// Adiciona a pontuação de cada palavra à lista 'scores'. A lista 'scores' é retornada
		List<Integer> scores = new ArrayList<Integer>();
		
		for(String word : words) {
			scores.add(calculateScore(word));
		}
		
		return scores;
	}
	
	/*
	 * word - palavra que desejo calcular a pontuação
	 * */
	public static Integer calculateScore(String word) {
		// Iteração sobre a palavra somando o valor de cada caractere a variável 'score'. O inteiro 'score' é retornado
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
		
		// Adiciona o bônus a 'score'
		score = calculateBonus(word, score);
		
		return score;
	}
	
	/*
	 * word - palavra que desejo aplicar o bônus de posição
	 * score - pontuação da palavra, sem o bônus adicionado
	 * */
	public static Integer calculateBonus(String word, Integer score) {
		// Se o jogador informou uma posição para aplicar o bônus, o valor do caracter na posição é somado a 'score'. O inteiro 'score' é retornada
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
		// Iteração sobre 'wordsDB' verificando se é possível formar a palavra através de 'userInput', adicionando à lista 'acceptedWords' quando
		// for possível a formação. A lista 'acceptedWords' é retornada
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
		// Iteração sobre os caracteres de 'sequence' removendo a primeira ocorrencia de 'sequence[i]' em 'word'
		Integer sequenceLen = sequence.length();
		
		for(int i = 0; i < sequenceLen; i++) {
			Integer index = word.indexOf(sequence.charAt(i));
			
			if(index != -1){
				word = word.replaceFirst(String.valueOf(sequence.charAt(i)), "");
			}
		}
		
		// Se 'wordLen = 0' é possível a formação de 'word' através de 'sequence' então retorna true se não, retorna false
		Integer wordLen = word.length();
		if( wordLen == 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/*
	 * words - Lista com todas as possíveis palavras formadas partindo de 'userInput'
	 * scores - Lista com a pontuação de cada palavra em 'words'
	 * */
	public static Map<String, String> bestScore(List<String> words, List<Integer> scores) {
		
		Integer biggestScore = 0, index = 0;
		String word = new String();
		
		// Iteração sobre a lista 'scores' comparando cada 'score' e atualizando 'biggestScore' quando necessário
		for(Integer score : scores) {
			// Palavra de 'wodrs' que corresponde a 'score'
			String currentWord = words.get(index);
			
			// Se 'score' é maior que 'biggestScore' substitui 'word' e 'biggestSocre' pelos correspondentes a 'score'
			if(score > biggestScore) {
				biggestScore = score;
				word = currentWord;
			}
			// Se forem iguais
			else if(score == biggestScore) {
				// Verifica qual palavra tem o maior tamanho e substitui 'word' e 'biggestSocre' pelos correspondentes à menor palavra
				if(currentWord.length() < word.length()) {
					word = currentWord;
					biggestScore = score;
				}
				// Se os tamanhos forem iguais
				else if(currentWord.length() == word.length()) {
					// Ordena em ordem alfabética e substitui 'word' e 'biggestSocre' pelos correspondentes a primeira palavra da ordem
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
		
		// Um map 'result' recebe a palavra com maior pontuação e seu score com as chaves "word" e "score" respectivamente
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
		
		// Iteração sobre 'word' caracter a caracter, removendo a primeira ocorrência de 'word[i]' em 'userInput', restando os caracteres
		// não utilizados
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
        	// Retorna a string letras em caixa auta, sem espaços e caracteres e acentos
        	letras = letras.replaceAll(" ","");
            return Normalizer.normalize(letras, Normalizer.Form.NFD)
                    .replaceAll("\\p{InCOMBINING_DIACRITICAL_MARKS}+", "").toUpperCase();
        }
    }

}
