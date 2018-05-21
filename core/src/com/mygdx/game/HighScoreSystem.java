package com.mygdx.game;

import com.badlogic.gdx.Gdx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;


class HighScoreSystem {

    private static String topPlayreName = "";
    private static int topPlayerScore = 0;

    public static int getTopPlayerScore() {
        return topPlayerScore;
    }

    public static String getTopPlayreName() {
        return topPlayreName;
    }

    static void loadTable(){
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = Gdx.files.local("hightscore.txt").reader(8192);
            String data = bufferedReader.readLine();
            topPlayreName = data.split(" ")[0];
            topPlayerScore =  Integer.parseInt(data.split(" ")[1]);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    static void createTable(){
        if(Gdx.files.local("hightscore.txt").exists()) {
            return;
        }
        Writer writer = null;
        try {
            writer = Gdx.files.local("hightscore.txt").writer(false);
            writer.write("unknown 0");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static void updateTable(String name, int score){
        if (score > topPlayerScore) {
            Writer writer = null;
            try {
                writer = Gdx.files.local("hightscore.txt").writer(false);
                writer.write(name + " " + score);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (writer != null) {
                        writer.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        loadTable();
    }



}
