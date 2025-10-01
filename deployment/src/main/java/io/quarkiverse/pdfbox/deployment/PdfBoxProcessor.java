package io.quarkiverse.pdfbox.deployment;

import io.quarkiverse.pdfbox.runtime.graal.PdfBoxFeature;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.NativeImageEnableAllCharsetsBuildItem;
import io.quarkus.deployment.builditem.NativeImageFeatureBuildItem;
import io.quarkus.deployment.builditem.nativeimage.NativeImageResourceDirectoryBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveHierarchyBuildItem;
import io.quarkus.deployment.builditem.nativeimage.RuntimeInitializedClassBuildItem;

class PdfBoxProcessor {

    private static final String FEATURE = "pdfbox";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    NativeImageFeatureBuildItem registerPdfBoxGraalFeature() {
        return new NativeImageFeatureBuildItem(PdfBoxFeature.class);
    }

    /**
     * Prevents the java.io.UnsupportedEncodingException: windows-1252 error in native mode
     */
    @BuildStep
    NativeImageEnableAllCharsetsBuildItem enableAllCharsets() {
        return new NativeImageEnableAllCharsetsBuildItem();
    }

    @BuildStep
    public void registerRuntimeInitializedClasses(BuildProducer<RuntimeInitializedClassBuildItem> resource) {
        //org.apache.tika.parser.pdf.PDFParser (https://issues.apache.org/jira/browse/PDFBOX-4548)
        resource.produce(new RuntimeInitializedClassBuildItem("org.apache.pdfbox.pdmodel.font.PDType1Font"));
        resource.produce(new RuntimeInitializedClassBuildItem("org.apache.pdfbox.text.LegacyPDFStreamEngine"));
        // This one starts some crypto stuff
        resource.produce(new RuntimeInitializedClassBuildItem("org.apache.pdfbox.pdmodel.encryption.PublicKeySecurityHandler"));
        // This is started by anybody doing graphics at startup time, including pdfbox instantiating an empty image
        resource.produce(new RuntimeInitializedClassBuildItem("sun.java2d.Disposer"));
    }

    @BuildStep
    public void registerNativeResources(BuildProducer<NativeImageResourceDirectoryBuildItem> resource) {
        resource.produce(new NativeImageResourceDirectoryBuildItem("org/apache/pdfbox/resources/afm"));
        resource.produce(new NativeImageResourceDirectoryBuildItem("org/apache/pdfbox/resources/glyphlist"));
        resource.produce(new NativeImageResourceDirectoryBuildItem("org/apache/fontbox/cmap"));
        resource.produce(new NativeImageResourceDirectoryBuildItem("org/apache/fontbox/unicode"));
    }

    @BuildStep
    void registerForReflection(BuildProducer<ReflectiveHierarchyBuildItem> reflectiveHierarchy) {
        reflectiveHierarchy.produce(ReflectiveHierarchyBuildItem.builder(PDParentTreeValue.class).build());
    }
}
