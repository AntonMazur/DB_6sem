package utilities;

import entities.Book;

import java.util.Arrays;
import java.util.Map;

public class BookUtil {
    private static BookUtil instance = new BookUtil();

    public static BookUtil getInstance() {
        return instance;
    }

    private BookUtil() { }

    private static Book[] getBooksByParams(Map<BookParam, String> bookParams, Book[] books){
        return (Book[]) Arrays.stream(books)
                .filter(x ->
                        bookParams
                                .entrySet()
                                .stream()
                                .allMatch(entry -> x.isParamContainsKey(entry.getKey(), entry.getValue()))).toArray();
    }

    public static Book[] getBooksByYearBouns(int lowerBound, int upperBound, Book[] books){
        return (Book[]) Arrays.stream(books)
                .filter(book ->
                        lowerBound < 0
                                ? true
                                : Double.valueOf(book.getYear()) >= lowerBound)
                .filter(book ->
                        upperBound < 0
                                ? true
                                : Double.valueOf(book.getYear()) <= upperBound)
                .toArray();
    }

}
