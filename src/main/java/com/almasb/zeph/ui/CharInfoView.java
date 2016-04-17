package com.almasb.zeph.ui;

import com.almasb.zeph.combat.Attribute;
import com.almasb.zeph.combat.Stat;
import com.almasb.zeph.entity.character.PlayerEntity;
import com.almasb.zeph.entity.character.control.PlayerControl;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.control.Accordion;
import javafx.scene.control.Separator;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class CharInfoView extends Accordion {

    public CharInfoView(PlayerEntity player) {
        Font font = Font.font("Lucida Console", 14);
        //Cursor cursorQuestion = new ImageCursor(R.assets.getTexture("ui/cursors/question.png").getImage(), 52, 10);

        VBox attrBox = new VBox(5);
        attrBox.setTranslateY(10);
        for (Attribute attr : Attribute.values()) {
            Text text = new Text();
            text.setFont(font);
            //text.setCursor(cursorQuestion);
            text.textProperty().bind(player.getAttributes().attributeProperty(attr).asString(attr.toString() + ": %-3d"));

            Text tooltipText = new Text(attr.getDescription());
            tooltipText.setFill(Color.WHITE);
            tooltipText.setFont(Font.font(16));
            tooltipText.setWrappingWidth(200);

            Tooltip tooltip = new Tooltip();
            tooltip.setGraphic(tooltipText);

            Tooltip.install(text, tooltip);

            Text bText = new Text();
            bText.setFont(font);
            bText.setFill(Color.DARKGREEN);
            bText.visibleProperty().bind(player.getAttributes().bAttributeProperty(attr).greaterThan(0));
            bText.textProperty().bind(player.getAttributes().bAttributeProperty(attr).asString("+%d ")
                    .concat(player.getAttributes().totalAttributeProperty(attr).asString("(%d)")));

            Text btn = new Text("+");
            btn.setCursor(Cursor.HAND);
            btn.setStroke(Color.BLUE);
            btn.setStrokeWidth(3);
            btn.setFont(font);
            btn.visibleProperty().bind(player.getAttributePoints().greaterThan(0)
                    .and(player.getAttributes().attributeProperty(attr).lessThan(100)));

            btn.setOnMouseClicked(event -> {
                player.getControlUnsafe(PlayerControl.class).increaseAttribute(attr);
            });

            Pane box = new Pane();
            box.setPrefSize(160, 15);
            box.getChildren().addAll(text, bText, btn);

            bText.setTranslateX(70);
            btn.setTranslateX(155);

            attrBox.getChildren().add(box);
        }

        Text info = new Text();
        info.setFont(font);
        info.visibleProperty().bind(player.getAttributePoints().greaterThan(0));
        info.textProperty().bind(new SimpleStringProperty("Points: ").concat(player.getAttributePoints()));

        attrBox.getChildren().addAll(new Separator(), info);

        VBox statBox = new VBox(5);
        for (Stat stat : Stat.values()) {
            Text text = new Text();
            text.setFont(font);
            //text.setCursor(cursorQuestion);
            text.textProperty().bind(player.getStats().statProperty(stat).asString(stat.toString() + ": %d"));

            Text tooltipText = new Text(stat.getDescription());
            tooltipText.setFill(Color.WHITE);
            tooltipText.setFont(Font.font(16));
            tooltipText.setWrappingWidth(200);

            Tooltip tooltip = new Tooltip();
            tooltip.setGraphic(tooltipText);

            Tooltip.install(text, tooltip);

            Text bText = new Text();
            bText.setFont(font);
            bText.setFill(Color.DARKGREEN);

            StringBinding textBinding = Bindings.when(player.getStats().bStatProperty(stat).greaterThan(0))
                .then(player.getStats().bStatProperty(stat).asString("+%d ")
                        .concat(player.getStats().statProperty(stat).add(player.getStats().bStatProperty(stat)).asString("(%d)"))
                        .concat(stat.getMeasureUnit()))
                .otherwise(stat.getMeasureUnit());

            bText.textProperty().bind(textBinding);

            statBox.getChildren().add(new HBox(5, text, bText));
        }

        getPanes().add(new TitledPane("Char Info", new HBox(10, attrBox, new Separator(Orientation.VERTICAL), statBox)));
    }
}
