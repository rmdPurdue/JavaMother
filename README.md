# JavaMother

JavaMother is the controller-side of the IZZY Project. 

## Technology

* Recommended IDE: [IntelliJ](https://www.jetbrains.com/idea/) 

* Recommended UI Editor: [Scene Builder](https://www.jetbrains.com/help/idea/opening-fxml-files-in-javafx-scene-builder.html)

* SDK: [Oracle JDK 11.0.11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)

* GUI Framework: [JavaFX](https://openjfx.io/) version [11.0.2](https://gluonhq.com/products/javafx/)


## Installation (Windows)

1. [Download](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) or locate JDK 11 on your filesystem
2. [Download](https://gluonhq.com/products/javafx/) or locate JavaFX 11 jmods on your filesystem
3. Enter the following into a CMD window (make sure to replace the 3 fields with \<brackets\>) 
```bash
"<PATH TO JAVA JDK>\jdk-11.0.11\bin\jlink" --module-path "<PATH TO JAVAFX JMODS>\javafx-jmods-11.0.2" --add-modules java.se,javafx.fxml,javafx.web,javafx.media,javafx.swing --bind-services --output "<NEW JDK OUTPUT LOCATION>"
```

An EXAMPLE would be: (Don't copy this)

`"C:\Program Files\Java\jdk-11.0.11\bin\jlink" --module-path "C:\Program Files\Java\javafx-jmods-11.0.2" --add-modules java.se,javafx.fxml,javafx.web,javafx.media,javafx.swing --bind-services --output "C:\Users\eholl\OneDrive\Desktop\jdk-11.0.11+JavaFX"`

This creates a new JDK+JRE that includes all necessary packages.

4. Open IntelliJ and go to **File** > **Project Settings** > **SDKs** > **+** > **Add JDK**
5. Navigate to the output path you specified and click **OK**. Rename if desired.
6. Navigate to **Project Settings** > **Project**. Set **Project SDK** to your new JDK.
7. Click **OK**. You should now see that IntelliJ resolves all missing definitions.


8. When building, you may need to edit the build configuration to use this new JDK. 
