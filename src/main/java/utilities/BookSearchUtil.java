package utilities;

import entities.Book;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class BookSearchUtil {
    private static BookSearchUtil instance = new BookSearchUtil();

    public static BookSearchUtil getInstance() {
        return instance;
    }

    private BookSearchUtil() { }

    public static Stream<Book> getBooksByParams(Stream<Book> books, Map<BookParam, List<List<String>>> bookParams){
        return books
                .filter(x ->
                        bookParams
                                .entrySet()
                                .stream()
                                .allMatch(paramList -> paramList
                                        .getValue()
                                        .stream()
                                        .anyMatch(param -> param
                                                .stream()
                                                .allMatch(p -> x
                                                        .isParamContainsKey(paramList.getKey(), p)
                                                )
                                        )
                                )
                );
    }

    public static Stream<Book> getBooksByYearBounds(Stream<Book> books, int lowerBound, int upperBound) {
        return books
                .filter(book ->
                        lowerBound < 0
                            ? true
                            : book.getYear() >= lowerBound)
                .filter(book ->
                        upperBound < 0
                            ? true
                            : book.getYear() <= upperBound);
    }

    public static Stream<Book> getBooksByLanguage(Stream<Book> books, boolean isRussian, boolean isEnglish){
        if (isRussian && isEnglish){
            return books;
        }
        if (!isRussian && !isEnglish){
            return books.filter((b) -> false);
        }

        return books
                .filter(book -> isRussian
                                ? book.getLanguage() == Language.RUSSIAN
                                : book.getLanguage() == Language.ENGLISH);
    }

    public static Stream<Book> getBooksContainedIndexes(Stream<Book> books, boolean isContainsDOI, boolean isContainsSCOPULUS, boolean isContainsWOS) {
        if (isContainsDOI){
            books = books.filter(book -> book.getDOI() != "");
        }

        if (isContainsSCOPULUS){
            books = books.filter(book -> book.getSCOPULUS() != "");
        }

        if (isContainsWOS){
            books = books.filter(book -> book.getWOS() != "");
        }

        return books;
    }

    public static Stream<Book> getBooksBindedWithFiles(Stream<Book> books){
        return books.filter(book -> !book.getFileLink().equals(""));
    }

}
