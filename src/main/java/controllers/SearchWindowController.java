package controllers;

import entities.Book;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utilities.BookParam;
import utilities.BookSearchUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SearchWindowController implements Resultable{
    private static Stream<Book> filteringBooks;
    private Book[] filteredBooks;

    @FXML private TextField year;
    @FXML private TextField authors;
    @FXML private TextField name;
    @FXML private TextField edition;
    @FXML private TextField outputData;

    @FXML private CheckBox isRussian;
    @FXML private CheckBox isEnglish;
    @FXML private CheckBox isContainsDOI;
    @FXML private CheckBox isContainsSCOPULUS;
    @FXML private CheckBox isContainsWOS;
    @FXML private CheckBox isContainsFile;

    public static void setInitialBooks(Stream<Book> books){
        filteringBooks = books;
    }

    @FXML
    private void findBooksWithFilters(){
        StringBuilder errorsBuffer = new StringBuilder();
        trimAllTextFields();
        String currentFieldText = year.getText();
        if (!currentFieldText.equals("")){
            int hyphenIdx = currentFieldText.indexOf('-');
            int lowBound = Integer.MIN_VALUE;
            int upBound = Integer.MAX_VALUE;
            try{
                if (hyphenIdx == -1){
                    lowBound = Integer.parseInt(currentFieldText);
                    upBound = lowBound;
                }
                else {
                    if (hyphenIdx == 0){
                        upBound = Integer.parseInt(currentFieldText.replace("-", ""));
                    }
                    else {
                        if (hyphenIdx == currentFieldText.length() - 1){
                            lowBound = Integer.parseInt(currentFieldText.replace("-", ""));;
                        }
                        else {
                            String[] years = currentFieldText.split("-");
                            lowBound = Integer.parseInt(years[0]);
                            upBound = Integer.parseInt(years[1]);
                        }
                    }
                }
            } catch (NumberFormatException ex){
                errorsBuffer.append("'Year' field format mismatch to specified in brackets sample, so it will not impact on result\n");
            }
            filteringBooks = BookSearchUtil.getBooksByYearBounds(filteringBooks, lowBound, upBound);
        }
        Map<BookParam, List<String>> bookParams = new HashMap<>();

        currentFieldText = authors.getText();
        if (!currentFieldText.equals("")){
            bookParams.put(BookParam.AUTHORS, Arrays.asList(currentFieldText.split("\\|")));
        }

        currentFieldText = name.getText();
        if (!currentFieldText.equals("")){
            bookParams.put(BookParam.NAME, Arrays.asList(currentFieldText.split("\\|")));
        }

        currentFieldText = edition.getText();
        if (!currentFieldText.equals("")){
            bookParams.put(BookParam.EDITION, Arrays.asList(currentFieldText.split("\\|")));
        }

        currentFieldText = outputData.getText();
        if (!currentFieldText.equals("")){
            bookParams.put(BookParam.OUTPUT_DATA, Arrays.asList(currentFieldText.split("\\|")));
        }

        filteringBooks = BookSearchUtil.getBooksByParams(filteringBooks, bookParams);
        filteringBooks = BookSearchUtil
                .getBooksByLanguage(
                        filteringBooks, 
                        isRussian.isSelected(),
                        isEnglish.isSelected());
        filteringBooks = BookSearchUtil
                .getBooksContainedIndexes(
                        filteringBooks,
                        isContainsDOI.isSelected(),
                        isContainsSCOPULUS.isSelected(),
                        isContainsWOS.isSelected());

        if (isContainsFile.isSelected()){
            filteringBooks = BookSearchUtil.getBooksBindedWithFiles(filteringBooks);
        }

        filteredBooks = filteringBooks.toArray(Book[]::new);
        close();
    }

    private void trimAllTextFields(){
        year.setText(year.getText().trim());
        authors.setText(authors.getText().trim());
        name.setText(name.getText().trim());
        edition.setText(edition.getText().trim());
        outputData.setText(outputData.getText().trim());
    }

    @Override
    public Object getResult() {
        return filteredBooks;
    }

    private void close(){
        ((Stage) year.getScene().getWindow()).close();
    }
}
