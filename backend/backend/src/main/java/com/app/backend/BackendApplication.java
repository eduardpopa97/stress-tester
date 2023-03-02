package com.app.backend;

//import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//import com.app.backend.gui.Main;
//
//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Scene;
//import javafx.stage.Stage;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
		//Application.launch();
	}
	
//	@Override
//	   public void init() throws IOException {
//	        SpringApplication.run(getClass()).
//	        getAutowireCapableBeanFactory().
//	        autowireBean(this);
//	}
//	
//	@Override
//	    public void start(Stage primaryStage) throws Exception {
//			FXMLLoader fxmlLoader = new FXMLLoader(Main.class.
//					getResource("view/StressTestingGUI.fxml"));
//			Scene scene = new Scene(fxmlLoader.load(), 808, 678);
//			Stage stage = new Stage();
//			stage.setTitle("Stress Testing App");
//			stage.setScene(scene);
//			stage.show();
//			
//	    }
	
}
