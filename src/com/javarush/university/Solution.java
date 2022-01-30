package com.javarush.university;

import java.io.IOException;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class Solution {
    public static final Scanner SCANNER = new Scanner(System.in);
    public static final String CRYPTOGRAPHIC_ALPHABET = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя.,:;!? ";

    public static void main(String[] args) {
        selectAction();
    }

    private static void selectAction() {
        String programMenu = " Выберите действие, введя его номер:\n" +
                "1 - Зашифровать текст в файле с помощью ключа.\n" +
                "2 - Расшифровать текст в файле с помощью ключаю.\n" +
                "3 - Расшифровать текст в файле методом перебора ключа (брут-форс).\n" +
                "4 - Расшифровать текст в файле методом статистического анализа.\n" +
                "5 - Выйти из программы.\n" +
                " ";
        System.out.println(programMenu);
        int action = Integer.parseInt(SCANNER.nextLine());
        switch (action) {
            case 1:
                encrypt();
                break;
            case 2:
                decrypt();
                break;
            case 3:
                brutForce();
                break;
            case 4:
                statAnalyze();
                break;
            case 5:
                System.exit(0);
            default:
                System.out.println("Неверная цифра!");
        }
    }

    private static String getFileContent(String filePath) {
        Path path = Path.of(filePath);
        try {
            byte[] bytes = Files.readAllBytes(path);
            String fileContent = new String(bytes);
            String fileContentL = fileContent.toLowerCase();
            return fileContent;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void writeToFileE(String content, String prevFilePath, String suffix) {
        int dotIndex = prevFilePath.lastIndexOf(".");
        String fileBeforeDot = prevFilePath.substring(0, dotIndex);
        String fileAfterDot = prevFilePath.substring(dotIndex);
        String newFileName = fileBeforeDot + suffix + fileAfterDot;
        try {
            Files.writeString(Path.of(newFileName), content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeToFileD(String content, String prevFilePath, String suffix) {
        int dotIndex = prevFilePath.lastIndexOf(".");
        String fileBeforeDot = prevFilePath.substring(0, dotIndex);
        String fileAfterDot = prevFilePath.substring(dotIndex);
        int dashIndex = prevFilePath.lastIndexOf("-");
        String fileBeforeDash = prevFilePath.substring(0, dashIndex);
        String newFileName = fileBeforeDash + suffix + fileAfterDot;
        try {
            Files.writeString(Path.of(newFileName), content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void encrypt() {
        System.out.println("Запуск шифрования...");
        System.out.println("Введите полный путь к файлу:");
        String filePath = SCANNER.nextLine();
        String fileContent = getFileContent(filePath);
        System.out.println("Введите ключ шифрования от 0 до 20:");
        int keyEncoder = SCANNER.nextInt();
        writeToFileE(encryptText(fileContent, keyEncoder), filePath, "-encrypted");
    }

    private static String encryptText(String text, int keyEncoder) {
        StringBuilder stringBuilder = new StringBuilder();
        String textL = text.toLowerCase();
        for (int i = 0; i < textL.length(); i++) {
            char ch = textL.charAt(i);
            int charIndex = CRYPTOGRAPHIC_ALPHABET.indexOf(ch);
            int newCharIndex = charIndex + keyEncoder;
            newCharIndex = newCharIndex % CRYPTOGRAPHIC_ALPHABET.length();
            char encryptedChar = CRYPTOGRAPHIC_ALPHABET.charAt(newCharIndex);
            stringBuilder.append(encryptedChar);
        }
        return stringBuilder.toString();
    }

    private static void decrypt() {
        System.out.println("Запуск дешифрования...");
        System.out.println("Введите полный путь к файлу:");
        String filePath = SCANNER.nextLine();
        String fileContent = getFileContent(filePath);
        System.out.println("Введите ключ дешифрования от 0 до 20:");
        int keyDecoder = SCANNER.nextInt();
        writeToFileD(decryptText(fileContent, keyDecoder), filePath, "-decrypted");
    }

    private static String decryptText(String text, int keyDecoder) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            int charIndex = CRYPTOGRAPHIC_ALPHABET.indexOf(ch);
            int newCharIndex = charIndex + (CRYPTOGRAPHIC_ALPHABET.length() - keyDecoder);
            newCharIndex = newCharIndex % CRYPTOGRAPHIC_ALPHABET.length();
            char encryptedChar = CRYPTOGRAPHIC_ALPHABET.charAt(newCharIndex);
            stringBuilder.append(encryptedChar);
        }
        return stringBuilder.toString();

    }

    private static boolean isValidText(String text) {
//        char[] textchars = text.toCharArray();
//        int count = 0;
//        for (int i = 0; i < textchars.length; i++) {
//            for (int j = i + 1; j < textchars.length; j++) {
//                if (textchars[i] == '.' && textchars[j] == ' ') {
//                    count++;
//                }
//            }
//            if (count > 50) {
//                return true;
//            } else if (i == textchars.length - 1) {
//                return false;
//            }
//        }


//      проверка слов в тексте на количество символов
        String[] arrayStrings = text.split(" ");
        for (String arrayString : arrayStrings) {
            if (arrayString.length() > 24) {
                return false;
            }
        }
        System.out.println("Понятен ли Вам этот текст?");
        int stringStart = new Random().nextInt(text.length() / 2);
        int stringEnd = stringStart + 300;
        System.out.println(text.substring(stringStart, stringEnd));
        System.out.println("1 - Да");
        System.out.println("2 - Нет");
        int i = SCANNER.nextInt();
        if (i == 1) {
            return true;
        } else if (i == 2) {
            return false;
        } else {
            System.out.println("Неверный ввод!");
        }
        return false;
    }

    private static void brutForce() {
        System.out.println("Запуск метода брут-форс...");
        System.out.println("Введите полный путь к файлу:");
        String filePath = SCANNER.nextLine();
        String fileContent = getFileContent(filePath);
        for (int i = 0; i < CRYPTOGRAPHIC_ALPHABET.length(); i++) {
            String decryptedText = decryptText(fileContent, i);
            boolean isValid = isValidText(decryptedText);
            if (isValid) {
                System.out.println("Ключ шифрования - " + i);
                writeToFileD(decryptedText, filePath, "-brutted");
                break;
            }
        }
    }

    private static void statAnalyze() {
        System.out.println("Запуск статистического анализа...");
        System.out.println("Введите полный путь к файлу, который необходимо расшифровать:");
        String filePath = SCANNER.nextLine();
        System.out.println("Введите полный путь к файлу, для получения статистики:");
        String statFilePath = SCANNER.nextLine();
        String decryptedText = getFileContent(filePath);
        String textForStatistics = getFileContent(statFilePath);
        HashMap<Character, Integer> decryptedTextStatistics = getCharacterStatistics(decryptedText);
        HashMap<Character, Integer> generalStatistics = getCharacterStatistics(textForStatistics);
        HashMap<Character, Character> characterStatistics = getCharacterStatistics(decryptedTextStatistics, generalStatistics);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < decryptedText.length(); i++) {
            char decrChar = characterStatistics.get(decryptedText.charAt(i));
            result.append(decrChar);
        }
        writeToFileD(decryptedText, filePath, "-statanalyze");
    }

    private static HashMap<Character, Integer> getCharacterStatistics(String text) {
        HashMap<Character, Integer> resultAbsolute = new HashMap<>();
        HashMap<Character, Integer> result = new HashMap<>();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            Integer integer = resultAbsolute.get(ch);
            if (integer == null) {
                resultAbsolute.put(ch, 1);
            } else {
                integer++;
                resultAbsolute.put(ch, integer);
            }
        }
        for (Character character : resultAbsolute.keySet()) {
            Integer integer = resultAbsolute.get(character);
            int quantity = integer * 10000 / text.length();
            result.put(character, quantity);
        }
        return result;
    }

    private static HashMap<Character, Character> getCharacterStatistics(HashMap<Character, Integer> decryptedTextStatistics, HashMap<Character, Integer> generalStatistics) {
        HashMap<Character, Character> result = new HashMap<>();
        for (int i = 0; i < CRYPTOGRAPHIC_ALPHABET.length(); i++) {
            char ch = CRYPTOGRAPHIC_ALPHABET.charAt(i);
            Integer characterStat = generalStatistics.get(ch);
            Character closeCharacterFromStatMap = getCloseCharacterFromStatMap(decryptedTextStatistics, characterStat);
            result.put(closeCharacterFromStatMap,ch);
        }
        return result;
    }

    private static Character getCloseCharacterFromStatMap(HashMap<Character, Integer> statMap, Integer value) {
        Integer minDelta = Integer.MAX_VALUE;
        Character curentChar = 'c';
        if (statMap != null) {
            for (Map.Entry<Character, Integer> characterIntegerEntry : statMap.entrySet()) {
                int delta = Math.abs(characterIntegerEntry.getValue() - value);
                if (delta < minDelta) {
                    minDelta = delta;
                    curentChar = characterIntegerEntry.getKey();
                }
            }
        }
        return curentChar;
    }

}

