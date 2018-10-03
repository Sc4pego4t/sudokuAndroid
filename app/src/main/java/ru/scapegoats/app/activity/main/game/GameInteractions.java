package ru.scapegoats.app.activity.main.game;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.transition.Transition;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import androidx.annotation.NonNull;

import ru.scapegoats.app.R;
import ru.scapegoats.app.activity.main.MainActivity;
import ru.scapegoats.app.modules.SettingsPreferences;
import ru.scapegoats.app.modules.dialogs.DialogGameEnds;

public class GameInteractions {

    private MainActivity activity;
    private String EMPTY="";

    public GameInteractions(MainActivity activity) {
        this.activity = activity;
    }

    Map<String,Integer> gameSettings;


    public void setSettings(Map<String,Integer> settings){
        gameSettings=settings;
        //if it's not the first turn
        if(selectedCol>-1) {
            highlight(selectedRow, selectedCol);
            checkUserMistakes();
        }
    }
    public void startGame(@NonNull LinearLayout buttonContainer1, @NonNull LinearLayout container){
        initButtonPanel(buttonContainer1);
        initGrid(container);
        fill();
        shuffle();
        erase();
    }

    @NonNull
    private ArrayList<CoupleInt> insertHistory=new ArrayList<>();

    private void checkUserMistakes() {
        for (CoupleInt el : insertHistory) {
            if (gameSettings.get(SettingsPreferences.HIGHLIGHTMISTAKES) == SettingsPreferences.CHECKED
                    && findMistake(el.getV1(), el.getV2())) {
                cellsArray[el.getV1()][el.getV2()].setTextColor(activity.getResources().getColor(R.color.errorColor));
            } else {
                cellsArray[el.getV1()][el.getV2()].setTextColor(activity.getResources().getColor(R.color.colorAccent));
            }
        }
    }

    public void eraseCell(){
        if(selectedCol!=-1) {
            cellsArray[selectedRow][selectedCol].setText(EMPTY);
            deleteFromHistory(selectedRow, selectedCol);
        }
    }

    private void checkEnd(){
        int countFilledCells=insertHistory.size() + erasedCellsCount;
        boolean isMistake=true;
        if(countFilledCells==SIZE*SIZE){
            isMistake=false;
            for(CoupleInt el: insertHistory){
                isMistake=findMistake(el.getV1(),el.getV2());
            }
        }
        //if all fields are filled without mistakes
        if(!isMistake){
            DialogGameEnds.showDialog(activity);
        }
    }

    public void backToPreviousState(){
        int lastElIndex=insertHistory.size()-1;
        if(lastElIndex!=-1) {
            CoupleInt lastSelEl = insertHistory.get(lastElIndex);
            cellsArray[lastSelEl.getV1()][lastSelEl.getV2()].setText("");
            insertHistory.remove(lastElIndex);
        }
    }

    private void addToHistory(int row, int col){
        CoupleInt couple=new CoupleInt(row,col);
        //check is this object new
        boolean isNew=true;
        int index=0,i=0;
        if(insertHistory.size()>0) {
            for (CoupleInt el : insertHistory) {
                if (el.toString().equals(couple.toString())) {
                    isNew=false;
                    index=i;
                }
                i++;
            }
        }
        if (isNew){
            insertHistory.add(couple);
        } else {
            insertHistory.remove(index);
            insertHistory.add(couple);
        }
    }

    private void deleteFromHistory(int row, int col) {
        CoupleInt couple = new CoupleInt(row, col);
        int index = 0, i = 0;
        if (insertHistory.size() > 0) {
            for (CoupleInt el : insertHistory) {
                if (el.toString().equals(couple.toString())) {
                    index = i;
                }
                i++;
            }
        }
        insertHistory.remove(index);
    }


    private Button[][] cellsArray;
    private final int SIZE=9;
    private void initButtonPanel(@NonNull LinearLayout buttonContainer1){
        int margin=Math.round(activity.getResources().getDimension(R.dimen.margin03x));
        for(int i=1;i<=SIZE;i++) {

            Button button = new Button(activity);

            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.MATCH_PARENT);
            params.weight=1;
            params.setMargins(margin,margin,margin,margin);

            button.setText(String.valueOf(i));
            button.setPadding(0,button.getPaddingTop(),0,button.getPaddingBottom());
            button.setTextSize(Math.round(activity.getResources().getDimension(R.dimen.buttonTextSize)));
            button.setTextColor(activity.getResources().getColor(R.color.colorAccent));
            button.setBackground(activity.getResources().getDrawable(R.drawable.outlined_button));

            button.setLayoutParams(params);
            button.setOnClickListener(new ButtonClickListener(i));
            buttonContainer1.addView(button);
        }
    }


    private void initGrid(LinearLayout container) {
        splitterSize = Math.round(activity.getResources().getDimension(R.dimen.splitterSize));
        splitterColor = activity.getResources().getColor(R.color.gray);
        cellsArray = new Button[9][9];

        int margin=Math.round(activity.getResources().getDimension(R.dimen.margin));
        int displayWidth = activity.getWindowManager().getDefaultDisplay().getWidth();
        ViewGroup.LayoutParams params = container.getLayoutParams();
        params.height = displayWidth-margin*2;
        container.setLayoutParams(params);

        for (int row = 0; row < SIZE; row++) {

            LinearLayout newRow = new LinearLayout(activity);
            LinearLayout.LayoutParams paramsLayout;
            paramsLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            paramsLayout.weight = 1;

            newRow.setLayoutParams(paramsLayout);
            drawHorizontalLine(container,row);
            for (int col = 0; col < SIZE; col++) {
                drawVerticalLine(newRow, col,row,false);
                Button button = new Button(activity);

                LinearLayout.LayoutParams paramsButton = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
                paramsButton.weight = 1;

                button.setBackgroundColor(activity.getResources().getColor(R.color.background));
                button.setLayoutParams(paramsButton);
                button.setTextSize(activity.getResources().getDimension(R.dimen.cellTextSize));
                button.setGravity(Gravity.CENTER);
                button.setTextColor(activity.getResources().getColor(R.color.colorAccent));



                button.setOnClickListener(new CellClickListener(row,col));
                newRow.addView(button);
                cellsArray[row][col] = button;
            }
            drawVerticalLine(newRow, SIZE, SIZE,false);
            container.addView(newRow);
        }
        drawHorizontalLine(container,SIZE);
        paint();
    }

    class CellClickListener implements View.OnClickListener{
        int row,col;

        private CellClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void onClick(View view) {
            highlight(row,col);
        }
    }

    class ButtonClickListener implements View.OnClickListener{
        int value;

        ButtonClickListener(int value) {
            this.value = value;
        }

        @Override
        public void onClick(View view) {
            //if we already clicked on some cell before
            if(selectedCol!=-1) {
                changeCellValue(value);
                addToHistory(selectedRow, selectedCol);
                checkUserMistakes();
                checkEnd();
            }
        }
    }

    private int selectedRow=-1,selectedCol=-1;



    class HighlightAnimator implements ValueAnimator.AnimatorUpdateListener{
        int row,col;

        public HighlightAnimator(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            cellsArray[row][col].setBackgroundColor((int) valueAnimator.getAnimatedValue());
            cellsArray[selectedRow][selectedCol].setBackgroundColor(activity.getResources().getColor(R.color.selectedColor));
        }
    }

    public void cancelAnimation(){
        if(animatorsList!=null) {
            for (ValueAnimator animator : animatorsList) {
                animator.cancel();
            }
        }
    }
    private ArrayList<ValueAnimator> animatorsList;

    private final int INITIAL_ANIMATION_DURATION=300;
    private final int ANIMATION_INCREMENT=150;

    private int createAndStartAnimator(int v1,int v2, int duration){
        duration+=ANIMATION_INCREMENT;
        int fromColor=activity.getResources().getColor(R.color.background);
        if(((ColorDrawable)cellsArray[v1][v2].getBackground()).getColor()!=fromColor){
            Log.e("YAYA","YES");
            fromColor=activity.getResources().getColor(R.color.lightBlue);
        }
        ValueAnimator animator=ValueAnimatorFactory.getAnimator(duration,
                fromColor,
                activity.getResources().getColor(R.color.highlightColor));
        animator.addUpdateListener(new HighlightAnimator(v1,v2));
        animator.start();
        animatorsList.add(animator);
        return duration;
    }

    private void highlight(int selectedRow, int selectedCol){

        this.selectedRow=selectedRow;
        this.selectedCol=selectedCol;
        cancelAnimation();
        paint();
        //check setting for highlight areas
        if(gameSettings.get(SettingsPreferences.HIGHLIGHTAREAS)==SettingsPreferences.CHECKED) {
            animatorsList=new ArrayList<>();

            int duration=INITIAL_ANIMATION_DURATION;
            //create animations to row and columns
            for (int col = selectedCol; col < SIZE; col++) {
                duration=createAndStartAnimator(selectedRow,col,duration);
            }
            duration=INITIAL_ANIMATION_DURATION;
            for (int col = selectedCol; col >= 0; col--) {
                duration=createAndStartAnimator(selectedRow,col,duration);
            }
            duration=INITIAL_ANIMATION_DURATION;
            for (int row = selectedRow; row < SIZE; row++) {
                duration=createAndStartAnimator(row,selectedCol,duration);
            }
            duration=INITIAL_ANIMATION_DURATION;
            for (int row = selectedRow; row >= 0; row--) {
                duration=createAndStartAnimator(row,selectedCol,duration);
            }
        }

        //check setting for highlight blocks
        if(gameSettings.get(SettingsPreferences.HIGHLIGHTBLOCK)==SettingsPreferences.CHECKED) {
            CoupleInt rowScopes=areaScopes(selectedRow);
            CoupleInt columnScopes=areaScopes(selectedCol);
            for(int i=rowScopes.getV1();i<=rowScopes.getV2();i++){
                for(int j=columnScopes.getV1();j<=columnScopes.getV2();j++){
                    cellsArray[i][j].setBackgroundColor(activity.getResources().getColor(R.color.highlightColor));
                }
            }
        }


    }

    private void changeCellValue(int value){
        cellsArray[selectedRow][selectedCol].setText(String.valueOf(value));
    }

    @NonNull
    private ArrayList<Integer> list = new ArrayList<Integer>() {{
        add(0);
        add(3);
        add(6);
        add(9);
    }};
    private int splitterSize;
    private int splitterColor;

    private void drawVerticalLine(@NonNull LinearLayout rowLayout, int column, int row, boolean isInHorizontalLine){
        int oldSize=splitterSize;
        int oldColor=splitterColor;
        if(list.contains(column)){
            splitterSize*=2;
            splitterColor=activity.getResources().getColor(R.color.darkGray);
        }
        if(list.contains(row) && isInHorizontalLine){
            splitterColor=activity.getResources().getColor(R.color.darkGray);
        }
        TextView splitter = new TextView(activity);
        LinearLayout.LayoutParams paramsSplitter = new LinearLayout.LayoutParams(splitterSize, LinearLayout.LayoutParams.MATCH_PARENT);
        splitter.setLayoutParams(paramsSplitter);
        splitter.setBackgroundColor(splitterColor);

        rowLayout.addView(splitter);
        splitterSize=oldSize;
        splitterColor=oldColor;

    }
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
                splitterColor = activity.getResources().getColor(R.color.darkGray);

            }
            TextView splitter = new TextView(activity);

            LinearLayout.LayoutParams paramsSplitter = new LinearLayout.LayoutParams(0, splitterSize);
            paramsSplitter.weight = 1;

            splitter.setBackgroundColor(splitterColor);
            splitter.setLayoutParams(paramsSplitter);
            newRow.addView(splitter);
            splitterSize=oldSize;
            splitterColor=oldColor;
        }
        drawVerticalLine(newRow,SIZE,row,true);
        container.addView(newRow);

    }

    private void paint(int row, int col) {
        if (row >= 0 && row < 3 || row >= 6 && row < 9) {
            if (col >= 0 && col < 3 || col >= 6 && col < 9) {
                cellsArray[row][col].setBackgroundColor(activity.getResources().getColor(R.color.lightBlue));
            } else {
                cellsArray[row][col].setBackgroundColor(activity.getResources().getColor(R.color.background));
            }
        } else {
            if (col > 2 && col < 6) {
                cellsArray[row][col].setBackgroundColor(activity.getResources().getColor(R.color.lightBlue));
            } else {
                cellsArray[row][col].setBackgroundColor(activity.getResources().getColor(android.R.color.white));
            }
        }
    }

    private void paint() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                paint(row, col);
            }
        }
    }

    private void fill(){
        int newRowOffset=3; //смещение при переводе строки
        int newTriadeOffset=4;//new triade offset
        int i=1;
        int j=0;
        for (Button[] row : cellsArray){
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
    private void shuffle(){
        int upper=8,lower=0;
        Random random=new Random();

        for (int j=0;j<100;j++){

            int num1=random.nextInt(upper-lower+1)+lower;

            CoupleInt temp=areaScopes(num1);
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


    private int erasedCellsCount=0;
    private void erase(){ //инициализация и генерация поля
        Set<Integer> erasedCells=new HashSet<>();
        int upper=8,lower=0;
        Random random=new Random();

        int difficulty=gameSettings.get(SettingsPreferences.DIFFICULTY)+1;


        for (int z=0;z<difficulty;z++){
            for (int x=0;x<SIZE;x++){
                for (int c=0;c<3;c++){
                    int randNum=random.nextInt(upper-lower+1)+lower;
                    erasedCells.add(x*10+randNum);
                    cellsArray[x][randNum].setText("");
                }
            }
        }

        for (int row=0; row<SIZE; row++){
            for (int col=0; col<SIZE; col++){
                if (!erasedCells.contains(row*10+col)){
                    //print(row*9+col)
                    erasedCellsCount++;
                    cellsArray[row][col].setEnabled(false);
                    cellsArray[row][col].setTextColor(activity.getResources().getColor(android.R.color.black));
                }
            }
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


    private CoupleInt areaScopes(int num) {
        if (num <= 2) {
            return new CoupleInt(0, 2);
        } else if (num <= 5) {
            return new CoupleInt(3, 5);
        } else {
            return new CoupleInt(6, 8);
        }
    }

    private String getText(Button button) {
        return button.getText().toString();
    }

    private void swapRowInArea(int numRow1, int numRow2) {
        for (int i = 0; i <= 8; i++) {
            String temp = getText(cellsArray[numRow1][i]);
            cellsArray[numRow1][i].setText(getText(cellsArray[numRow2][i]));
            cellsArray[numRow2][i].setText(temp);
        }
    }

    private void swapColumnInArea(int numColumn1, int numColumn2) {
        for (int i = 0; i <= 8; i++) {
            String temp = getText(cellsArray[i][numColumn1]);
            cellsArray[i][numColumn1].setText(getText(cellsArray[i][numColumn2]));
            cellsArray[i][numColumn2].setText(temp);
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


    private boolean findMistake(int row, int col){
        //проверка блока в котором находится нажимаемая клетка
        boolean isMistake=false;
        int checkedValue=Integer.parseInt(cellsArray[row][col].getText().toString());

        CoupleInt scopesRow=areaScopes(row);
        CoupleInt scopesCol=areaScopes(col);
        ArrayList<Integer> temp= new ArrayList<>();
        for (int r=scopesRow.getV1();r<=scopesRow.getV2();r++){
            for (int c=scopesCol.getV1();c<=scopesCol.getV2();c++){
                try {
                    //check for empty cells
                    int num = Integer.parseInt(cellsArray[r][c].getText().toString());
                    if(temp.contains(num) && num==checkedValue){
                        isMistake=true;

                    } else{
                        temp.add(num);
                    }
                } catch (Exception e){

                }
            }
        }
        temp.clear();
        //проверка строки в которой находится нажимаемая клетка
        for (int i=0;i<SIZE;i++){
            try {
                int num = Integer.parseInt(cellsArray[row][i].getText().toString());

                if(temp.contains(num) && num==checkedValue){
                    isMistake=true;
                } else{
                    temp.add(num);
                }
            } catch (Exception e) {

            }
        }
        temp.clear();
        //проверка столбца в котором находится нажимаемая клетка
        for (int i=0;i<SIZE;i++) {
            try {
                int num = Integer.parseInt(cellsArray[i][col].getText().toString());
                if(temp.contains(num) && num==checkedValue){
                    isMistake=true;
                } else{
                    temp.add(num);
                }
            } catch (Exception e) {

            }
        }
        return isMistake;
    }
}
