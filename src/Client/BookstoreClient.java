package Client;

import Domain.Book;
import Domain.Chapter;
import Domain.Publisher;
import Service.BookstoreService;

import java.util.ArrayList;
import java.util.List;

public class BookstoreClient {
    //how to manually map an object to a database
    public static void main(String[] args) {
        BookstoreService bookstoreService = new BookstoreService();

        //persist object graph
        Publisher publisher = new Publisher("MANN", "Manning Publications Co");

        Book book = new Book("9781617290459", "Java Persistence with Hibernate, Second Edition", publisher);

        List<Chapter> chapters = new ArrayList<Chapter>();
        Chapter chapter1 = new Chapter("Introducing JPA and Hibernate", 1);
        chapters.add(chapter1);
        Chapter chapter2 = new Chapter("Domain Models and Metadata", 2);
        chapters.add(chapter2);

        book.setChapters(chapters);
        bookstoreService.persistObjectGraph(book);

        //retrieve object graph
//        Book book1 = bookstoreService.retrieveObjectGraph("9781617290459");
//        System.out.println(book1);
    }
}
