package ar.com.kfgodel.objectmapper.tests;

import ar.com.dgarcia.javaspec.api.JavaSpec;
import ar.com.dgarcia.javaspec.api.JavaSpecRunner;
import ar.com.dgarcia.javaspec.api.TestContext;
import org.junit.runner.RunWith;

/**
 * This type verifies the minimum basic mapper
 * Created by kfgodel on 18/11/14.
 */
@RunWith(JavaSpecRunner.class)
public class ConversionPerformanceIT extends JavaSpec<TestContext> {
    @Override
    public void define() {
        describe("measuring conversion times", () -> {

            describe("from object to map", () -> {
                it("native mapper",()->{

                });
                it("jackson mapper",()->{

                });
                it("implementation mapper",()->{

                });
            });


            describe("from map to object", () -> {
                it("native mapper",()->{

                });
                it("jackson mapper",()->{

                });
                it("implementation mapper",()->{

                });
            });
        });
    }
}