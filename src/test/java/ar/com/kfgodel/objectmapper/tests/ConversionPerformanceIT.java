package ar.com.kfgodel.objectmapper.tests;

import ar.com.dgarcia.javaspec.api.JavaSpec;
import ar.com.dgarcia.javaspec.api.JavaSpecRunner;
import ar.com.dgarcia.javaspec.api.TestContext;
import ar.com.dgarcia.objectmapper.api.TypeMapper;
import ar.com.dgarcia.objectmapper.impl.EnsembleMapper;
import ar.com.dgarcia.objectmapper.impl.JacksonMapper;
import ar.com.dgarcia.objectmapper.impl.TransformerMapper;
import ar.com.kfgodel.objectmapper.tests.mapper.CustomMadeTypicalObjectAssembler;
import ar.com.kfgodel.objectmapper.tests.mapper.CustomMadeTypicalObjectDisassembler;
import ar.com.kfgodel.objectmapper.tests.mapper.TypicalObjectMapper;
import ar.com.kfgodel.objectmapper.tests.testObjects.TypicalObject;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This type verifies the minimum basic mapper
 * Created by kfgodel on 18/11/14.
 */
@RunWith(JavaSpecRunner.class)
public class ConversionPerformanceIT extends JavaSpec<TestContext> {
    public static Logger LOG = LoggerFactory.getLogger(ConversionPerformanceIT.class);
    public static long CONVERSION_COUNT = 1_000_000;

    @Override
    public void define() {
        describe("measuring conversion times", () -> {
            
            LOG.info("Conversions per test: " + CONVERSION_COUNT);

            describe("from object to map", () -> {
                it("warm up", () -> {
                    TypicalObject typicalObject = TypicalObject.create();
                    typicalObject.initializeRootTestData();

                    TypicalObjectMapper testedMapper = TypicalObjectMapper.create();
                    Map<String, Object> converted = null;
                    for (int i = 0; i < CONVERSION_COUNT; i++) {
                        converted = testedMapper.toMap(typicalObject);
                    }
                    assertThat(converted).isEqualTo(TypicalObject.createRootTestMap());
                });
                it("custom made mapper", () -> {
                    runDisassemblyTestsOn(TypicalObjectMapper.create());
                });
                it("ensemble mapper", () -> {
                    EnsembleMapper ensembleMapper = EnsembleMapper.create();
                    ensembleMapper.getDisassembler().getDisassemblyTransformer().addTransformerFor(TypicalObject.class, CustomMadeTypicalObjectDisassembler.create());
                    runDisassemblyTestsOn(ensembleMapper);
                });
                it("transformer mapper",()->{
                    TransformerMapper transformerMapper = TransformerMapper.create();
                    transformerMapper.getDisassemblyTransformer().addTransformerFor(TypicalObject.class, CustomMadeTypicalObjectDisassembler.create());
                    runDisassemblyTestsOn(transformerMapper);
                });
                it("jackson mapper", () -> {
                    runDisassemblyTestsOn(JacksonMapper.create());
                });
            });


            describe("from map to object", () -> {
                it("warm up",()->{
                    Map<String, Object> testMap = TypicalObject.createRootTestMap();

                    TypicalObjectMapper testedMapper = TypicalObjectMapper.create();
                    TypicalObject converted = null;
                    for (int i = 0; i < CONVERSION_COUNT; i++) {
                        converted = testedMapper.fromMap(testMap, TypicalObject.class);
                    }
                    TypicalObject expected = TypicalObject.create();
                    expected.initializeRootTestData();
                    assertThat(converted).isEqualTo(expected);
                });
                it("custom made mapper",()->{
                    runAssemblyTestsOn(TypicalObjectMapper.create());
                });
                it("ensemble mapper",()->{
                    runAssemblyTestsOn(EnsembleMapper.create());
                });
                it("transformer mapper",()->{
                    TransformerMapper transformerMapper = TransformerMapper.create();
                    transformerMapper.getAssemblyTransformer().addTransformerFor(TypicalObject.class, CustomMadeTypicalObjectAssembler.create());
                    runAssemblyTestsOn(transformerMapper);
                });   
                it("jackson mapper",()->{
                    runAssemblyTestsOn(JacksonMapper.create());
                });
            });
        });
    }

    private void runDisassemblyTestsOn(TypeMapper testedMapper) {
        TypicalObject typicalObject = TypicalObject.create();
        typicalObject.initializeRootTestData();


        long started = System.nanoTime();
        Map<String, Object> converted = null;
        for (int i = 0; i < CONVERSION_COUNT; i++) {
            converted = testedMapper.toMap(typicalObject);
        }
        long ended = System.nanoTime();
        long elapsedMillis = TimeUnit.NANOSECONDS.toMillis(ended - started);
        long conversionsPerSecond = (long) (CONVERSION_COUNT / (elapsedMillis / 1000d));
        LOG.info("Disassembly {}: {} ms, {}/s", new Object[]{testedMapper.getClass().getSimpleName() , elapsedMillis, conversionsPerSecond});
        assertThat(converted).isEqualTo(TypicalObject.createRootTestMap());
    }

    private void runAssemblyTestsOn(TypeMapper testedMapper) {

        Map<String, Object> testMap = TypicalObject.createRootTestMap();

        long started = System.nanoTime();
        TypicalObject converted = null;
        for (int i = 0; i < CONVERSION_COUNT; i++) {
            converted = testedMapper.fromMap(testMap, TypicalObject.class);
        }
        long ended = System.nanoTime();
        long elapsedMillis = TimeUnit.NANOSECONDS.toMillis(ended - started);
        long conversionsPerSecond = (long) (CONVERSION_COUNT / (elapsedMillis / 1000d));
        LOG.info("Assembly {}: {} ms, {}/s", new Object[]{testedMapper.getClass().getSimpleName() , elapsedMillis, conversionsPerSecond} );
        TypicalObject expected = TypicalObject.create();
        expected.initializeRootTestData();
        assertThat(converted).isEqualTo(expected);
    }
}