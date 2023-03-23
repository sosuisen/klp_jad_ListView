package com.example;

import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MyApp extends Application {

	@Override
	public void start(Stage stage) {
		var tField = new TextField();
		var listView = new ListView<String>();
		// 観察可能なリスト
		// イベントハンドラを追加することで、変更があったときに処理を行うことができます。
		ObservableList<String> model = listView.getItems();

		// model変更時に呼ばれるイベントハンドラの追加。
		// 単にラムダ式 c -> {} と書ければ良いのですが、
		// ObserbavleListのイベントハンドラには
		// ListChangeListenerとInvalidationListenerの２通りがあるため、
		// ListChangeListenerのほうであることがコンパイラに判るよう型を書く必要があります。
		model.addListener((ListChangeListener.Change<? extends String> c) -> {
			// 変更箇所のみファイルへ保存する、といった処理ができます。
			// 今回はコンソールへ出力するにとどめます。
			while (c.next()) {
				// cにはモデルの変更内容がリストとして入っているため、whileで一つずつ取り出します。
				if (c.wasReplaced()) {
					// getAddedSublist()で今回追加があった内容の取り出し。
					// リストの項目を書き換えた場合は、旧内容の削除、新内容の追加が行われるので、
					// 追加分をgetAddedSublist()で取り出すことができます。
					c.getAddedSubList().forEach(str -> System.out.println("書き換え[" + c.getFrom() + "] " + str));
				} else if (c.wasAdded()) {
					c.getAddedSubList().forEach(str -> System.out.println("追加[" + c.getFrom() + "]" + str));
				}
			}
			/* 変更のたびにモデルの全内容を出力するならこんな感じ。 
			model.forEach(str -> {
				System.out.println(str);
			});
			*/
		});

		// 編集できるようにする
		listView.setEditable(true);
		// セルファクトリクラスを指定
		listView.setCellFactory(TextFieldListCell.forListView());
		// 編集完了時に呼ばれるイベントハンドラを追加
		listView.setOnEditCommit(e -> {
			// 編集されたセルの値を取得
			String newStr = e.getNewValue();
			// 何番目のセルが編集されたかを取得
			int index = e.getIndex();
			// modelのindex番目の値をnewStrに変更する
			model.set(index, newStr);
		});

		tField.setOnAction(e -> {
			model.add(tField.getText());
			tField.clear();
		});

		var vBox = new VBox();
		vBox.getChildren().add(tField);
		vBox.getChildren().add(listView);

		var scene = new Scene(vBox, 320, 200);
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch();
	}

}