module cz.cvut.fel.pjv.gamejava {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.annotation;
    requires java.desktop;
    requires javafx.media;

    exports cz.cvut.fel.pjv.main;
    exports cz.cvut.fel.pjv.inputs;
    exports cz.cvut.fel.pjv.utils;
    exports cz.cvut.fel.pjv.levels;
    exports cz.cvut.fel.pjv.characters;
}