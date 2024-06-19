package com.ezen.diary;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Calendar;

public class Diary extends Frame {

    MenuBar menuBar;
    Menu fileMenu;
    MenuItem newMI, openMI, saveMI, exitMI;
    Label timeL, statusL;
    TextArea contentsTA;

    private static int contentLength = 0;

    public Diary() {
        this("제목없음");
    }

    public Diary(String title) {
        super(title);
        menuBar = new MenuBar();
        fileMenu = new Menu("파일");
        newMI = new MenuItem("새로 만들기");
        openMI = new MenuItem("열기");
        saveMI = new MenuItem("저장");
        exitMI = new MenuItem("끝내기");

        timeL = new Label("", Label.RIGHT);
        contentsTA = new TextArea();
        statusL = new Label();
        statusL.setBackground(Color.LIGHT_GRAY);
    }

    /**
     * GUI 구현을 위한 기능
     */

    public void init() {
        setMenuBar(menuBar);
        menuBar.add(fileMenu);
        fileMenu.add(newMI);
        fileMenu.add(openMI);
        fileMenu.add(saveMI);
        fileMenu.addSeparator(); //("--------") 메뉴 구분하기위해 사용하는 메소드
        fileMenu.add(exitMI);
        newMI.setShortcut(new MenuShortcut(KeyEvent.VK_N));
        openMI.setShortcut(new MenuShortcut(KeyEvent.VK_O));
        saveMI.setShortcut(new MenuShortcut(KeyEvent.VK_S));
        exitMI.setShortcut(new MenuShortcut(KeyEvent.VK_X));
        add(timeL, BorderLayout.NORTH);
        add(contentsTA, BorderLayout.CENTER);
        add(statusL, BorderLayout.SOUTH);
    }

    /**
     * 초기 화면에 현재시간 출력하는 기능
     */

    private void setTime() {
        timeL.setBackground(new Color(64, 128, 125));
        timeL.setForeground(Color.WHITE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String format = String.format("%1$tF %1$tT (%1$tA)", Calendar.getInstance());
                    timeL.setText(format);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }

            }
        }).start();
    }
    private void setNew() {
        contentsTA.setText("");
    }

    /**
     * 저장된 파일 읽는 기능
     * 파일 다이얼로그에서 선택한 파일의 내용을 읽어서(문자 스트림), textArea 에 출력
     */

    private void openFile() {
        FileDialog fileDialog = new FileDialog(this, "일기장 열기", FileDialog.LOAD);
		fileDialog.setVisible(true);

        String openFileName = fileDialog.getFile();
        String openDirectory = fileDialog.getDirectory();

        if (openFileName != null && openDirectory != null) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(openDirectory + openFileName))) {
                StringBuilder stringBuilder = new StringBuilder();
                String Textline;
                while ((Textline = bufferedReader.readLine()) != null) {
                    stringBuilder.append(Textline).append("\n");
                }
                contentsTA.setText(stringBuilder.toString());
            } catch (IOException ex) {
                System.out.println("파일 열기에 실패하였습니다.");
            }
        }
    }

    /**
     * 작성한 텍스트파일을 저장하는 기능
     * 저장하기를 누르면 현재시간을 제목 끝에 같이 출력
     * @throws FileNotFoundException 파일을 찾지 못했을 경우 예외처리
     */

    private void saveFile() throws FileNotFoundException {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(calendar.YEAR);
        int month = calendar.get(calendar.MONTH);
        int day = calendar.get(calendar.DAY_OF_MONTH);
        String fileName = "일기장_"+year+month+day+"txt";

        FileDialog fileDialog = new FileDialog(this, "일기장 저장", FileDialog.SAVE);
        fileDialog.setFile(fileName);

        fileDialog.setVisible(true);

        String saveFileName = fileDialog.getFile();
        String saveDirectory = fileDialog.getDirectory();

        if (saveFileName != null && saveDirectory != null) {
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(saveDirectory + saveFileName))) {
                bufferedWriter.write(contentsTA.getText());
            } catch (IOException ex) {
                System.out.println("저장에 실패 하였습니다.");
            }
        }
    }

    /**
     *  프로그램을 종료하는 기능 처리
     */
    
    private void exit() {
        setVisible(false);
        dispose();
        System.exit(0);
    }

    /**
     * 이벤트 처리를 하는 기능
     */

    public void addEventListener() {
        // Window Event  처리
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }

            @Override
            public void windowOpened(WindowEvent e) {
                setTime();
            }
        });

        // Action Event 처리
        newMI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setNew();
            }
        });

        openMI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        });

        saveMI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    saveFile();
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        exitMI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });
    }
}
