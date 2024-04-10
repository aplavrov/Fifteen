package com.example.android.fifteen;

import android.content.DialogInterface;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static android.view.View.INVISIBLE;

public class MainActivity extends AppCompatActivity implements
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {

    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;
    private TextView txt;
    private GestureDetectorCompat gd;
    int[] position = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    int[] winPosition = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0};
    int[] blocks = {
            R.drawable.empty,
            R.drawable.one,
            R.drawable.two,
            R.drawable.three,
            R.drawable.four,
            R.drawable.five,
            R.drawable.six,
            R.drawable.seven,
            R.drawable.eight,
            R.drawable.nine,
            R.drawable.ten,
            R.drawable.eleven,
            R.drawable.twelve,
            R.drawable.thirteen,
            R.drawable.fourteen,
            R.drawable.fifteen
    };
    private TableLayout tableLayout;
    int BOOKSHELF_ROWS = 4;
    int BOOKSHELF_COLUMNS = 4;
    private ImageButton ib;
    private Button newButton;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);

        newButton = findViewById(R.id.button);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGame();
            }
        });
        newButton.setVisibility(View.INVISIBLE);

        gd = new GestureDetectorCompat(this, this);
        gd.setOnDoubleTapListener(this);

        Random random = new Random();
        for (int i = position.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int a = position[index];
            position[index] = position[i];
            position[i] = a;
        }
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);

        ib = (ImageButton) findViewById(R.id.imageButton);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ib.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.INVISIBLE);
                newButton.setVisibility(View.VISIBLE);
            }
        });

        depictTable();
    }

    public void newGame () {
        ib.setVisibility(View.VISIBLE);
        Random random = new Random();
        for (int i = position.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int a = position[index];
            position[index] = position[i];
            position[i] = a;
        }
        imageView.setVisibility(View.VISIBLE);
        newButton.setVisibility(View.INVISIBLE);
        depictTable();
    }

    public void depictTable() {
        tableLayout.removeAllViews();
        for (int i = 0; i < BOOKSHELF_ROWS; i++) {

            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

            for (int j = 0; j < BOOKSHELF_COLUMNS; j++) {
                ImageView imageView = new ImageView(this);
                imageView.setImageResource(blocks[position[i * 4 + j]]);

                tableRow.addView(imageView, j);
            }

            tableLayout.addView(tableRow, i);
        }
        if (Arrays.equals(position, winPosition)) {
            AlertDialog.Builder a_builder = new AlertDialog.Builder(MainActivity.this);
            a_builder.setMessage("Вы победили!")
                    .setCancelable(false)
                    .setPositiveButton("Новая игра", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            newGame();
                        }
                    })
                    .setNegativeButton("Назад", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = a_builder.create();
            alert.setTitle("Победа");
            alert.show();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gd.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        boolean result = false;
        try {
            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        onSwipeRight();
                    } else {
                        onSwipeLeft();
                    }
                    result = true;
                }
            } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffY > 0) {
                    onSwipeBottom();
                } else {
                    onSwipeTop();
                }
                result = true;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return result;
    }

    public void onSwipeTop() {
        boolean check = false;
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                if (position[i * 4 + j] == 0 && i < 3) {
                    int a = position[i * 4 + j];
                    position[i * 4 + j] = position[(i + 1) * 4 + j];
                    position[(i + 1) * 4 + j] = a;
                    check = true;
                    break;
                }
            }
            if (check) break;
        }
        depictTable();
    }

    public void onSwipeRight() {
        for (int i = 0; i < 4; ++i)
            for (int j = 0; j < 4; ++j) {
                if (position[i * 4 + j] == 0 && j > 0) {
                    int a = position[i * 4 + j];
                    position[i * 4 + j] = position[i * 4 + j - 1];
                    position[i * 4 + j - 1] = a;
                    break;
                }
            }
        depictTable();
    }

    public void onSwipeLeft() {
        for (int i = 0; i < 4; ++i)
            for (int j = 0; j < 4; ++j) {
                if (position[i * 4 + j] == 0 && j < 3) {
                    int a = position[i * 4 + j];
                    position[i * 4 + j] = position[i * 4 + j + 1];
                    position[i * 4 + j + 1] = a;
                    break;
                }
            }
        depictTable();
    }

    public void onSwipeBottom() {
        boolean check = false;
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                if (position[i * 4 + j] == 0 && i > 0) {
                    int a = position[i * 4 + j];
                    position[i * 4 + j] = position[(i - 1) * 4 + j];
                    position[(i - 1) * 4 + j] = a;
                    check = true;
                    break;
                }
            }
            if (check) break;
        }
        depictTable();
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }
}
