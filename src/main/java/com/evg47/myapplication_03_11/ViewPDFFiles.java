package com.evg47.myapplication_03_11;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.evg47.myapplication_03_11.app.activities.MainActivity;
import com.evg47.myapplication_03_11.app.data.JSONHelper;
import com.evg47.myapplication_03_11.app.data.MyBooks;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;

import java.util.ArrayList;
import java.util.List;

public class ViewPDFFiles extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener, OnPageErrorListener {

    PDFView pdfView;
    int position = -1;
    private int numPage = 0;
    Toast toast;
    MyBooks myBooks;
    private List<MyBooks> myBooksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdffiles);

        pdfView = (PDFView) findViewById(R.id.pdfView);
        position = getIntent().getIntExtra("position", -1);

        numPage = LoadMyBooksList();

        displayPDF();
    }

    private void displayPDF() {
        pdfView.fromFile(MainActivity.fileList.get(position))
                .defaultPage(numPage)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))//справа штучка
                .spacing(10)
                .onPageError(this)
                .pageFitPolicy(FitPolicy.BOTH)
                .load();
    }

    public void saveLastPage() {
        boolean NewBook = true;
        String NewNameBook = MainActivity.fileList.get(position).toString();
        int NewPageBook = pdfView.getCurrentPage();

        myBooks = new MyBooks(NewNameBook, NewPageBook);
        for (int i = 0; i < myBooksList.size(); i++) {
            if (myBooksList.get(i).getBookName().equals(myBooks.getBookName())) {
                myBooksList.get(i).setLastPage(NewPageBook);
                NewBook = false;
                break;
            }
        }
        if (NewBook) {
            myBooksList.add(myBooks);
        }
        JSONHelper.exportToJSON(this, myBooksList);

    }

    public int LoadMyBooksList() {
        myBooksList = JSONHelper.importFromJSON(this);
        if (myBooksList != null) {
            for (int i = 0; i < myBooksList.size(); i++) {
                if (MainActivity.fileList.get(position).toString().equals(myBooksList.get(i).getBookName())) {
                    return myBooksList.get(i).getLastPage();
                }
            }
        } else {
            myBooksList = new ArrayList<>();
        }
        return 0;
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveLastPage();
        Log.i("MyBooks", myBooks.getBookName() + " " + myBooks.getLastPage());
        Toast.makeText(getApplicationContext(), " " + pdfView.getCurrentPage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        toast = Toast.makeText(this, +page + " pageCount " + pageCount, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void loadComplete(int nbPages) {
        toast = Toast.makeText(this, "loadComplete", Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onPageError(int page, Throwable t) {
        toast = Toast.makeText(this, "onPageError", Toast.LENGTH_LONG);
        toast.show();
    }
}
