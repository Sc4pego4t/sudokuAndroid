package ru.scapegoats.app.activity.main.game;

import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import androidx.annotation.NonNull;
import ru.scapegoats.app.R;
import ru.scapegoats.app.activity.main.MainActivity;
import ru.scapegoats.app.activity.main.game.misc.CoupleInt;
import ru.scapegoats.app.activity.main.game.misc.MyColorPalette;
import ru.scapegoats.app.modules.SettingsPreferences;

public class GameInitialization {
    private MainActivity activity;
    MyColorPalette colorPalette;
    GameInteractions gameInteractions;

    GameInitialization(MainActivity activity,MyColorPalette colorPalette, GameInteractions gameInteractions){
        this.activity=activity;
        this.colorPalette=colorPalette;
        this.gameInteractions=gameInteractions;
    }

    void initButtonPanel(@NonNull LinearLayout buttonContainer1){
        int margin=Math.round(activity.getResources().getDimension(R.dimen.margin03x));
        for(int i=1;i<=GameInteractions.SIZE;i++) {

            Button button = new Button(activity);

            gameInteractions.buttonsArray.add(button);

            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.MATCH_PARENT);
            params.weight=1;
            params.setMargins(margin,margin,margin,margin);

            button.setText(String.valueOf(i));
            button.setPadding(0,button.getPaddingTop(),0,button.getPaddingBottom());
            button.setTextSize(Math.round(activity.getResources().getDimension(R.dimen.buttonTextSize)));
            button.setTextColor(colorPalette.accent);
            if(gameInteractions.gameSettings.get(SettingsPreferences.Settings.Theme).equals(SettingsPreferences.Themes.Dark.index)){
                button.setBackground(activity.getResources().getDrawable(R.drawable.outlined_button_dark));
            } else{
                button.setBackground(activity.getResources().getDrawable(R.drawable.outlined_button));
            }

            button.setLayoutParams(params);

            button.setOnClickListener(gameInteractions.getNewButtonClickListener(i));
            buttonContainer1.addView(button);
        }
    }


    private int splitterSize;
    private int splitterColor;

    void initGrid(LinearLayout container) {
        splitterSize = Math.round(activity.getResources().getDimension(R.dimen.splitterSize));
        splitterColor = colorPalette.splitterThin;
        gameInteractions.cellsArray = new Button[9][9];

        int margin=Math.round(activity.getResources().getDimension(R.dimen.margin));
        int displayWidth = activity.getWindowManager().getDefaultDisplay().getWidth();
        ViewGroup.LayoutParams params = container.getLayoutParams();
        params.height = displayWidth-margin*2;
        container.setLayoutParams(params);

        for (int row = 0; row < GameInteractions.SIZE; row++) {

            LinearLayout newRow = new LinearLayout(activity);
            LinearLayout.LayoutParams paramsLayout;
            paramsLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            paramsLayout.weight = 1;

            newRow.setLayoutParams(paramsLayout);
            drawHorizontalLine(container,row);
            for (int col = 0; col < GameInteractions.SIZE; col++) {
                drawVerticalLine(newRow, col,row,false);
                Button button = new Button(activity);


                LinearLayout.LayoutParams paramsButton = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
                paramsButton.weight = 1;

                button.setBackgroundColor(colorPalette.background);
                button.setLayoutParams(paramsButton);

                button.setTextSize(activity.getResources().getDimension(R.dimen.cellTextSize));
                button.setGravity(Gravity.CENTER);
                button.setTextColor(colorPalette.accent);


                button.setOnClickListener(gameInteractions.getNewCellClickListener(row,col));
                newRow.addView(button);
                gameInteractions.cellsArray[row][col] = button;
            }
            drawVerticalLine(newRow, GameInteractions.SIZE, GameInteractions.SIZE,false);
            container.addView(newRow);
        }
        drawHorizontalLine(container,GameInteractions.SIZE);
        gameInteractions.paint();
    }

    void fill(){
        int newRowOffset=3; //смещение при переводе строки
        int newTriadeOffset=4;//new triade offset
        int i=1;
        int j=0;
        for (Button[] row : gameInteractions.cellsArray){
            j++;
            for(Button cell : row){
                cell.setText(String.valueOf(i));
                i=offset(i);
            }
            if(j%3==0) {
                i = offset(i, newTriadeOffset);
            } else {
                i = offset(i, newRowOffset);
            }
        }
    }

    void shuffle(){
        int upper=8,lower=0;
        Random random=new Random();

        for (int j=0;j<100;j++){

            int num1=random.nextInt(upper-lower+1)+lower;

            CoupleInt temp=gameInteractions.areaScopes(num1);
            lower=temp.getV1();
            upper=temp.getV2();

            int num2=random.nextInt(upper-lower+1)+lower;
            if (num1==num2){
                continue;
            }

            int numA1=(num1*10)%3;
            int numA2=(num2*10)%3;
            //перемешиваю
            swapRowInArea(num1, num2);
            swapColumnInArea(num1, num2);
            swapColumnArea(numA1, numA2);
            swapRowArea(numA1, numA2);
        }
    }
    void erase(){ //инициализация и генерация поля
        Set<Integer> erasedCells=new HashSet<>();
        int upper=8,lower=0;
        Random random=new Random();

        int difficulty=gameInteractions.gameSettings.get(SettingsPreferences.Settings.Difficulty)+1;




        for (int z=0;z<difficulty;z++){
            for (int x=0;x<GameInteractions.SIZE;x++){
                for (int c=0;c<3;c++){
                    int randNum=random.nextInt(upper-lower+1)+lower;
                    erasedCells.add(x*10+randNum);
                    gameInteractions.cellsArray[x][randNum].setText("");
                }
            }
        }

        for (int row=0; row<GameInteractions.SIZE; row++){
            for (int col=0; col<GameInteractions.SIZE; col++){
                if (!erasedCells.contains(row*10+col)){
                    //print(row*9+col)
                    gameInteractions.erasedCellsCount++;
                    gameInteractions.cellsArray[row][col].setEnabled(false);
                    gameInteractions.cellsArray[row][col].setTextColor(colorPalette.textColor);
                }
            }
        }

    }

    @NonNull
    private ArrayList<Integer> list = new ArrayList<Integer>() {{
        add(0);
        add(3);
        add(6);
        add(9);
    }};

    private void drawHorizontalLine(@NonNull LinearLayout container, int row){
        LinearLayout newRow=new LinearLayout(activity);
        LinearLayout.LayoutParams paramsLayout;
        paramsLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        newRow.setLayoutParams(paramsLayout);

        int oldSize=splitterSize;
        int oldColor=splitterColor;
        for(int i=0;i<9;i++) {
            drawVerticalLine(newRow,i,row, true);
            if (list.contains(row)) {
                splitterSize *= 2;
                splitterColor = colorPalette.splitterThick;

            }
            TextView splitter = new TextView(activity);

            if(splitterColor==colorPalette.splitterThick){
                gameInteractions.thickSplittersArray.add(splitter);
            } else {
                gameInteractions.thinSplittersArray.add(splitter);
            }

            LinearLayout.LayoutParams paramsSplitter = new LinearLayout.LayoutParams(0, splitterSize);
            paramsSplitter.weight = 1;

            splitter.setBackgroundColor(splitterColor);
            splitter.setLayoutParams(paramsSplitter);
            newRow.addView(splitter);
            splitterSize=oldSize;
            splitterColor=oldColor;
        }
        drawVerticalLine(newRow,GameInteractions.SIZE,row,true);
        container.addView(newRow);
    }

    private void drawVerticalLine(@NonNull LinearLayout rowLayout, int column, int row, boolean isInHorizontalLine){
        int oldSize=splitterSize;
        int oldColor=splitterColor;

        if(list.contains(column)){
            splitterSize*=2;
            splitterColor=colorPalette.splitterThick;
        }

        if(list.contains(row) && isInHorizontalLine){
            splitterColor=colorPalette.splitterThick;
        }


        TextView splitter = new TextView(activity);

        if(splitterColor==colorPalette.splitterThick){
            gameInteractions.thickSplittersArray.add(splitter);
        } else {
            gameInteractions.thinSplittersArray.add(splitter);
        }

        LinearLayout.LayoutParams paramsSplitter = new LinearLayout.LayoutParams(splitterSize, LinearLayout.LayoutParams.MATCH_PARENT);
        splitter.setLayoutParams(paramsSplitter);
        splitter.setBackgroundColor(splitterColor);

        rowLayout.addView(splitter);
        splitterSize=oldSize;
        splitterColor=oldColor;

    }

    private void swapRowInArea(int numRow1, int numRow2) {
        for (int i = 0; i <= 8; i++) {
            String temp = getText(gameInteractions.cellsArray[numRow1][i]);
            gameInteractions.cellsArray[numRow1][i].setText(getText(gameInteractions.cellsArray[numRow2][i]));
            gameInteractions.cellsArray[numRow2][i].setText(temp);
        }
    }

    private void swapColumnInArea(int numColumn1, int numColumn2) {
        for (int i = 0; i <= 8; i++) {
            String temp = getText(gameInteractions.cellsArray[i][numColumn1]);
            gameInteractions.cellsArray[i][numColumn1].setText(getText(gameInteractions.cellsArray[i][numColumn2]));
            gameInteractions.cellsArray[i][numColumn2].setText(temp);
        }
    }

    private void swapRowArea(int numRow1, int numRow2) {
        for (int j = 0; j <= 2; j++) {
            swapRowInArea(j + (numRow1 * 3), j + (numRow2 * 3));
        }
    }

    private void swapColumnArea(int numColumn1, int numColumn2) {
        for (int j = 0; j <= 2; j++) {
            swapColumnInArea(j + (numColumn1 * 3), j + (numColumn2 * 3));
        }
    }


    private int offset(int num) {
        if (num + 1 > 9) {
            return 1;
        } else {
            return num + 1;
        }
    }

    private int offset(int num, int count) {
        int result = num;
        if (num + count > 9) {
            for (int i = 0; i < count; i++) {
                result = offset(result);
            }
            return result;
        } else {
            return num + count;
        }
    }
    private String getText(Button button) {
        return button.getText().toString();
    }

}
