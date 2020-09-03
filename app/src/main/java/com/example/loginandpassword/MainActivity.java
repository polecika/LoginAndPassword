package com.example.loginandpassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText editLogin;
    private EditText editPassword;
    private Button btnLogin;
    private Button btnRegistration;
    private static ArrayList<String> countTracker;
    private static int count = 0;
    private static final String FILE_LOG_PAS = "file";
    private static final String FILE_NAME_ARRAY_LIST = "ArrayListFile";
    private static String checkInformationFileString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editLogin = findViewById(R.id.editLogin);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setClickable(false);
        btnRegistration = findViewById(R.id.btnRegistration);
        countTracker = new ArrayList<>();



        checkInformationFileString = readArrayFileFromInternalStorage();
        if (checkInformationFileString != null) {
            String[] splitCheckInformationFileString = checkInformationFileString.split(";");
            for(int i = 0; i < splitCheckInformationFileString.length; i++ ){
                countTracker.add(splitCheckInformationFileString[i]);
                count++;
            }
        }
        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(editLogin.getText().toString().equals("") || editPassword.getText().toString().equals(""))) {
                    String loginString = editLogin.getText().toString();
                    String passwordString = editPassword.getText().toString();
                    saveIntoInternalStorage(loginString, passwordString, count);
                    editLogin.setText("");
                    editPassword.setText("");
                    count++;
                } else {
                    Toast.makeText(MainActivity.this, "Не заполнена строка логина или пароля ", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!countTracker.isEmpty()) {
                    if (!(editLogin.getText().toString().equals("") || editPassword.getText().toString().equals(""))) {
                        String loginString = editLogin.getText().toString();
                        String passwordString = editPassword.getText().toString();
                        String enterAccount = loginString + "/n" + passwordString;

                        for (int i = 0; i < countTracker.size(); i++) {
                            String checkFileLoginPassword = readFromInternalStorage(countTracker.get(i));
                            if (checkFileLoginPassword.equals(enterAccount)) {
                                Toast.makeText(MainActivity.this, "Добро пожаловать " + loginString, Toast.LENGTH_SHORT).show();
                                editLogin.setText("");
                                editPassword.setText("");
                                break;
                            }
                            if(i == (countTracker.size() - 1)){
                        Toast.makeText(MainActivity.this, "логина или пароля нет в базе", Toast.LENGTH_SHORT).show();
                    }

                        }

                    } else {
                        Toast.makeText(MainActivity.this, "Не заполнена строка логина или пароля или нет в базе зарегестрированных пользователей", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "база логинов и паролей - пустая", Toast.LENGTH_SHORT).show();
                }}
            });
        }

        private void saveIntoInternalStorage (String login, String password,int numberOfRegistration)
        {

            countTracker.add(FILE_LOG_PAS + numberOfRegistration);
            BufferedWriter arrayWriter = null;
            try {
                arrayWriter = new BufferedWriter(new OutputStreamWriter(openFileOutput(FILE_NAME_ARRAY_LIST, Context.MODE_APPEND)));
                arrayWriter.append(FILE_LOG_PAS + numberOfRegistration + ";");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (arrayWriter != null) {
                    try {
                        arrayWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new OutputStreamWriter(openFileOutput(countTracker.get(numberOfRegistration), Context.MODE_APPEND)));
                writer.write(login + "/n" + password);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        private String readFromInternalStorage (String fileName){
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(openFileInput(fileName)));
                return reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        private String readArrayFileFromInternalStorage ()
        {
            BufferedReader reader = null;
            String text;
            try {
                reader = new BufferedReader(new InputStreamReader(openFileInput(FILE_NAME_ARRAY_LIST)));
                text = reader.readLine();
                return text;
            } catch (IOException e) {
                e.printStackTrace();

            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
    }