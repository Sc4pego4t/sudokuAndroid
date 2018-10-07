package ru.scapegoats.app.activity.main.game;

import android.animation.ValueAnimator;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Map;

import androidx.annotation.NonNull;

import ru.scapegoats.app.R;
import ru.scapegoats.app.activity.main.MainActivity;
import ru.scapegoats.app.activity.main.MainView;
import ru.scapegoats.app.activity.main.game.misc.CoupleInt;
import ru.scapegoats.app.activity.main.game.misc.MyColorPalette;
import ru.scapegoats.app.activity.main.game.misc.ValueAnimatorFactory;
import ru.scapegoats.app.modules.SettingsPreferences;
import ru.scapegoats.app.modules.dialogs.DialogGameEnds;

public class GameInteractions {

    MainActivity activity;
    private String EMPTY="";
    MyColorPalette colorPalette;


    public GameInteractions(MainActivity activity) {
        this.activity = activity;
    }

    Map<String,Integer> gameSettings;


    public void setSettings(Map<String,Integer> settings){
        gameSettings=settings;
        colorPalette=new MyColorPalette(activity,gameSettings.get(SettingsPreferences.THEME));
        if(initialized) {
            redraw();
        }
        //if it's not the first turn
        if(selectedCol>-1) {
            highlight(selectedRow, selectedCol);
            checkUserMistakes();
        }
    }

    //arrays to easiest redrawing
    ArrayList<TextView> thickSplittersArray = new ArrayList<>();
    ArrayList<TextView> thinSplittersArray = new ArrayList<>();
    ArrayList<Button> buttonsArray = new ArrayList<>();


    private void redraw(){

        for (TextView splitter:thinSplittersArray){
            splitter.setBackgroundColor(colorPalette.splitterThin);
        }

        for (TextView splitter:thickSplittersArray){
            splitter.setBackgroundColor(colorPalette.splitterThick);
        }

        for (Button button:buttonsArray){
            if(gameSettings.get(SettingsPreferences.THEME).equals(SettingsPreferences.THEME_DARK)) {
                button.setBackground(activity.getResources().getDrawable(R.drawable.outlined_button_dark));
            } else {
                button.setBackground(activity.getResources().getDrawable(R.drawable.outlined_button));
            }
            button.setTextColor(colorPalette.accent);
        }

        for(Button cellRow[]:cellsArray){
            for(Button cell: cellRow){
                if(cell.isEnabled()){
                    cell.setTextColor(colorPalette.accent);
                } else {
                    cell.setTextColor(colorPalette.textColor);
                }
            }
        }

        paint();
    }




    private boolean initialized=false;
    public void startGame(@NonNull LinearLayout buttonContainer1, @NonNull LinearLayout container){
        GameInitialization gameInitialization=new GameInitialization(activity,colorPalette,this);
        gameInitialization.initButtonPanel(buttonContainer1);
        gameInitialization.initGrid(container);
        gameInitialization.fill();
        gameInitialization.shuffle();
        gameInitialization.erase();
        initialized=true;

    }

    @NonNull
    private ArrayList<CoupleInt> insertHistory=new ArrayList<>();

    private void checkUserMistakes() {
        for (CoupleInt el : insertHistory) {
            if (gameSettings.get(SettingsPreferences.HIGHLIGHTMISTAKES) == SettingsPreferences.CHECKED
                    && findMistake(el.getV1(), el.getV2())) {
                cellsArray[el.getV1()][el.getV2()].setTextColor(colorPalette.error);
            } else {
                cellsArray[el.getV1()][el.getV2()].setTextColor(colorPalette.accent);
            }
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

    public void eraseCell(){
        if(selectedCol==-1) {
            return;
        }
        String cellText=cellsArray[selectedRow][selectedCol].getText().toString();
        if(cellText.equals("")){
            return;
        }

        cellsArray[selectedRow][selectedCol].setText(EMPTY);

        insertHistory.remove(new CoupleInt(selectedRow, selectedCol));
    }

    private void addToHistory(int row, int col){
        CoupleInt couple=new CoupleInt(row,col);
        if(insertHistory.contains(couple)){
            //add it to the end
            insertHistory.remove(couple);
        }
        insertHistory.add(couple);
        Log.e("hist",insertHistory.toString());
    }





    Button[][] cellsArray;
    static final int SIZE=9;



    CellClickListener getNewCellClickListener(int row,int cell){
        return new CellClickListener(row,cell);
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

    ButtonClickListener getNewButtonClickListener(int i){
        return new ButtonClickListener(i);
    }

    public class ButtonClickListener implements View.OnClickListener{
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

    private final int INITIAL_ANIMATION_DURATION=100;
    private final int ANIMATION_INCREMENT=100;
    private final int CELL_OFFSET=1;

    private int createAndStartAnimator(int v1,int v2, int duration){
        duration+=ANIMATION_INCREMENT;
        int fromColor=colorPalette.background;
        if(((ColorDrawable)cellsArray[v1][v2].getBackground()).getColor()!=fromColor){
            fromColor=colorPalette.secondary;
        }
        ValueAnimator animator=ValueAnimatorFactory.getAnimator(duration,
                fromColor,
                colorPalette.highlight);
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

            //if animation setting is on
            if(gameSettings.get(SettingsPreferences.ANIMATION)==SettingsPreferences.CHECKED) {
                int duration = INITIAL_ANIMATION_DURATION;
                //create animations to row and columns
                for (int col = selectedCol+CELL_OFFSET; col < SIZE; col++) {
                    duration = createAndStartAnimator(selectedRow, col, duration);
                }
                duration = INITIAL_ANIMATION_DURATION;
                for (int col = selectedCol-CELL_OFFSET; col >= 0; col--) {
                    duration = createAndStartAnimator(selectedRow, col, duration);
                }
                duration = INITIAL_ANIMATION_DURATION;
                for (int row = selectedRow+CELL_OFFSET; row < SIZE; row++) {
                    duration = createAndStartAnimator(row, selectedCol, duration);
                }
                duration = INITIAL_ANIMATION_DURATION;
                for (int row = selectedRow-CELL_OFFSET; row >= 0; row--) {
                    duration = createAndStartAnimator(row, selectedCol, duration);
                }
            } else {
                for (int i = 0; i < SIZE; i++) {
                    cellsArray[i][selectedCol].setBackgroundColor(colorPalette.highlight);
                    cellsArray[selectedRow][i].setBackgroundColor(colorPalette.highlight);
                }

            }
            cellsArray[selectedRow][selectedCol].setBackgroundColor(colorPalette.select);

        }

        //check setting for highlight blocks
        if(gameSettings.get(SettingsPreferences.HIGHLIGHTBLOCK)==SettingsPreferences.CHECKED) {
            //if animation setting is on

            CoupleInt rowScopes=areaScopes(selectedRow);
            CoupleInt columnScopes=areaScopes(selectedCol);
            for(int row=rowScopes.getV1();row<=rowScopes.getV2();row++){
                for(int col=columnScopes.getV1();col<=columnScopes.getV2();col++){
                    if(gameSettings.get(SettingsPreferences.ANIMATION)==SettingsPreferences.CHECKED) {
                        //check distances between selected and iterated cells
                        int duration=INITIAL_ANIMATION_DURATION+ANIMATION_INCREMENT;
                        if(row>selectedRow+CELL_OFFSET || row<selectedRow-CELL_OFFSET
                                || col>selectedCol+CELL_OFFSET || col<selectedCol-CELL_OFFSET){

                            duration+=ANIMATION_INCREMENT;
                        }
                        //TODO possible mistake check animations
                        //if cell is not animated in this block then animate
                        if(((ColorDrawable)cellsArray[row][col].getBackground()).getColor() == colorPalette.background
                                || ((ColorDrawable)cellsArray[row][col].getBackground()).getColor()== colorPalette.secondary){
                            createAndStartAnimator(row,col,duration);
                        }
                    }else {
                        cellsArray[row][col].setBackgroundColor(activity.getResources().getColor(R.color.blue));
                    }
                }
            }
        }
        cellsArray[selectedRow][selectedCol].setBackgroundColor(colorPalette.select);
    }

    private void changeCellValue(int value){
        cellsArray[selectedRow][selectedCol].setText(String.valueOf(value));
    }

    private void paint(int row, int col) {
        if (row >= 0 && row < 3 || row >= 6 && row < 9) {
            if (col >= 0 && col < 3 || col >= 6 && col < 9) {
                cellsArray[row][col].setBackgroundColor(colorPalette.secondary);
            } else {
                cellsArray[row][col].setBackgroundColor(colorPalette.background);
            }
        } else {
            if (col > 2 && col < 6) {
                cellsArray[row][col].setBackgroundColor(colorPalette.secondary);
            } else {
                cellsArray[row][col].setBackgroundColor(colorPalette.background);
            }
        }
    }

    void paint() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                paint(row, col);
            }
        }
    }

    int erasedCellsCount=0;

    CoupleInt areaScopes(int num) {
        if (num <= 2) {
            return new CoupleInt(0, 2);
        } else if (num <= 5) {
            return new CoupleInt(3, 5);
        } else {
            return new CoupleInt(6, 8);
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
