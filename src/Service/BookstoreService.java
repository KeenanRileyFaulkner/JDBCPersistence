package Service;

import Domain.Book;
import Domain.Chapter;
import Domain.Publisher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookstoreService {
    public static final String url = "jdbc:postgresql://localhost/BookStorePersistenceDemo";
    public static final String username = "postgres";
    public static final String pwd = "somedbpwd";

    public void persistObjectGraph(Book book) {
        try (Connection con = DriverManager.getConnection(url, username, pwd)) {
            PreparedStatement st = con.prepareStatement("insert into publisher (code, publisher_name) values (?, ?)");
            st.setString(1, book.getPublisher().getCode());
            st.setString(2, book.getPublisher().getName());
            st.executeUpdate();
            st.close();

            st = con.prepareStatement("insert into book (isbn, book_name, publisher_code) values (?, ?, ?)");
            st.setString(1, book.getIsbn());
            st.setString(2, book.getName());
            st.setString(3, book.getPublisher().getCode());
            st.executeUpdate();
            st.close();

            st = con.prepareStatement("insert into chapter (book_isbn, chapter_num, title) values (?, ?, ?)");
            for(Chapter chapter : book.getChapters()) {
                st.setString(1, book.getIsbn());
                st.setInt(2, chapter.getChapterNumber());
                st.setString(3, chapter.getTitle());
                st.executeUpdate();
            }

            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Book retrieveObjectGraph(String isbn) {
        Book book = null;

        try (Connection con = DriverManager.getConnection(url, username, pwd)) {
            PreparedStatement st
                    = con.prepareStatement("select * from book, publisher where book.publisher_code " +
                    "= publisher.code and book.isbn = ?");

            st.setString(1, isbn);
            ResultSet rs = st.executeQuery();

            book = new Book();
            if(rs.next()) {
                book.setIsbn(rs.getString("isbn"));
                book.setName(rs.getString("book_name"));

                Publisher publisher = new Publisher();
                publisher.setCode(rs.getString("code"));
                publisher.setName(rs.getString("publisher_name"));
                book.setPublisher(publisher);
            }

            List<Chapter> chapters = new ArrayList<Chapter>();
            st = con.prepareStatement("select * from chapter where book_isbn = ?");
            st.setString(1, isbn);
            rs = st.executeQuery();

            while(rs.next()) {
                Chapter chapter = new Chapter();
                chapter.setTitle(rs.getString("title"));
                chapter.setChapterNumber(rs.getInt("chapter_num"));
                chapters.add(chapter);
            }
            book.setChapters(chapters);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return book;
    }
}
