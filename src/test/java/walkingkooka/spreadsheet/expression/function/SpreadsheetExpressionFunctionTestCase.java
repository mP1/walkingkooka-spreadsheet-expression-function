
/*
 * Copyright 2022 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package walkingkooka.spreadsheet.expression.function;

import org.junit.jupiter.api.Test;
import walkingkooka.convert.Converters;
import walkingkooka.convert.provider.ConverterSelector;
import walkingkooka.environment.AuditInfo;
import walkingkooka.locale.LocaleContexts;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.TypeNameTesting;
import walkingkooka.spreadsheet.SpreadsheetContexts;
import walkingkooka.spreadsheet.engine.SpreadsheetMetadataMode;
import walkingkooka.spreadsheet.environment.SpreadsheetEnvironmentContext;
import walkingkooka.spreadsheet.environment.SpreadsheetEnvironmentContexts;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContexts;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.meta.store.FakeSpreadsheetMetadataStore;
import walkingkooka.spreadsheet.meta.store.SpreadsheetMetadataStore;
import walkingkooka.spreadsheet.reference.FakeSpreadsheetExpressionReferenceLoader;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.store.SpreadsheetCellStore;
import walkingkooka.spreadsheet.store.SpreadsheetCellStores;
import walkingkooka.spreadsheet.store.repo.FakeSpreadsheetStoreRepository;
import walkingkooka.spreadsheet.value.SpreadsheetCell;
import walkingkooka.storage.Storages;
import walkingkooka.tree.expression.ExpressionEvaluationContexts;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.function.ExpressionFunctionTesting;

import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

public abstract class SpreadsheetExpressionFunctionTestCase<F extends SpreadsheetExpressionFunction<T>, T>
    implements ExpressionFunctionTesting<F, T, SpreadsheetExpressionEvaluationContext>,
    ClassTesting2<F>,
    TypeNameTesting<F>,
    SpreadsheetMetadataTesting {

    final static SpreadsheetCellReference REFERENCE = SpreadsheetSelection.parseCell("Z99");

    final static SpreadsheetCellReference LOAD_CELL_REFERENCE = SpreadsheetSelection.parseCell("M31");

    final static String LOAD_FORMULA_TEXT = "='loaded formulatext result";

    final static SpreadsheetCell LOAD_CELL = SpreadsheetCell.with(
        LOAD_CELL_REFERENCE,
        SpreadsheetFormula.EMPTY.setText(LOAD_FORMULA_TEXT)
    );

    final static SpreadsheetCell CELL_EMPTY_FORMULA = SpreadsheetCell.with(
        SpreadsheetSelection.parseCell("E5"),
        SpreadsheetFormula.EMPTY.setText("")
    );

    final static SpreadsheetFormula FORMULA = SpreadsheetFormula.EMPTY
        .setText("=1+2");

    final static SpreadsheetCell CELL = SpreadsheetCell.with(
        REFERENCE,
        FORMULA
    );

    final static ExpressionNumberKind KIND = SpreadsheetMetadataTesting.EXPRESSION_NUMBER_KIND;

    SpreadsheetExpressionFunctionTestCase() {
        super();
    }

    @Test
    public final void testIsPure() {
        final F function = this.createBiFunction();
        final String name = function.name()
            .get()
            .value()
            .toLowerCase();

        this.isPureAndCheck(
            function,
            ExpressionEvaluationContexts.fake(),
            !(name.equals("cell") || name.equals("offset"))
        );
    }

    final SpreadsheetExpressionEvaluationContext createContext0() {
        final Locale locale = Locale.ENGLISH;

        final SpreadsheetId spreadsheetId = SpreadsheetId.parse("1234");

        final SpreadsheetMetadata metadata = SpreadsheetMetadata.EMPTY
            .set(SpreadsheetMetadataPropertyName.SPREADSHEET_ID, spreadsheetId)
            .set(SpreadsheetMetadataPropertyName.SPREADSHEET_NAME, SpreadsheetName.with("Untitled5678"))
            .set(SpreadsheetMetadataPropertyName.LOCALE, locale)
            .loadFromLocale(
                LocaleContexts.jre(locale)
            )
            .set(
                SpreadsheetMetadataPropertyName.AUDIT_INFO,
                AuditInfo.create(
                    EmailAddress.parse("creator@example.com"),
                    LocalDateTime.of(1999, 12, 31, 12, 58, 59)
                )
            ).set(SpreadsheetMetadataPropertyName.CELL_CHARACTER_WIDTH, 1)
            .set(SpreadsheetMetadataPropertyName.DATE_TIME_OFFSET, Converters.EXCEL_1904_DATE_SYSTEM_OFFSET)
            .set(SpreadsheetMetadataPropertyName.DEFAULT_YEAR, 20)
            .set(SpreadsheetMetadataPropertyName.EXPRESSION_NUMBER_KIND, KIND)
            .set(SpreadsheetMetadataPropertyName.FORMULA_CONVERTER, ConverterSelector.parse("collection(text, number, basic, spreadsheet-value)"))
            .set(SpreadsheetMetadataPropertyName.PRECISION, MathContext.DECIMAL32.getPrecision())
            .set(SpreadsheetMetadataPropertyName.ROUNDING_MODE, RoundingMode.HALF_UP)
            .set(SpreadsheetMetadataPropertyName.NUMBER_FORMATTER, SpreadsheetPattern.parseNumberFormatPattern("#.###").spreadsheetFormatterSelector())
            .set(SpreadsheetMetadataPropertyName.TEXT_FORMATTER, SpreadsheetPattern.parseTextFormatPattern("@@").spreadsheetFormatterSelector())
            .set(SpreadsheetMetadataPropertyName.TWO_DIGIT_YEAR, 20)
            .setDefaults(SpreadsheetMetadata.NON_LOCALE_DEFAULTS);

        final SpreadsheetEnvironmentContext spreadsheetEnvironmentContext = SpreadsheetEnvironmentContexts.basic(
            Storages.tree(),
            SPREADSHEET_ENVIRONMENT_CONTEXT.cloneEnvironment()
        );
        spreadsheetEnvironmentContext.setSpreadsheetId(
            Optional.of(spreadsheetId)
        );

        return SpreadsheetExpressionEvaluationContexts.spreadsheetContext(
            SpreadsheetMetadataMode.FORMULA,
            Optional.of(CELL),
            new FakeSpreadsheetExpressionReferenceLoader() {
                @Override
                public Optional<SpreadsheetCell> loadCell(final SpreadsheetCellReference cell,
                                                          final SpreadsheetExpressionEvaluationContext context) {
                    if (LOAD_CELL_REFERENCE.equals(cell)) {
                        return Optional.of(LOAD_CELL);
                    }
                    if (CELL_EMPTY_FORMULA.reference().equals(cell)) {
                        return Optional.of(CELL_EMPTY_FORMULA);
                    }
                    return Optional.empty();
                }
            },
            SPREADSHEET_LABEL_NAME_RESOLVER,
            SpreadsheetContexts.fixedSpreadsheetId(
                SPREADSHEET_ENGINE,
                new FakeSpreadsheetStoreRepository() {

                    @Override
                    public SpreadsheetCellStore cells() {
                        return this.cells;
                    }

                    private final SpreadsheetCellStore cells = SpreadsheetCellStores.treeMap();

                    @Override
                    public SpreadsheetMetadataStore metadatas() {
                        return new FakeSpreadsheetMetadataStore() {
                            @Override
                            public Optional<SpreadsheetMetadata> load(final SpreadsheetId id) {
                                return Optional.ofNullable(
                                    id.equals(spreadsheetId) ?
                                        metadata :
                                        null
                                );
                            }
                        };
                    }
                },
                (c) -> {
                    throw new UnsupportedOperationException();
                }, // Function<SpreadsheetContext, SpreadsheetEngineContext> spreadsheetEngineContextFactory
                (c) -> {
                    throw new UnsupportedOperationException();
                }, // Function<SpreadsheetEngineContext, Router<HttpRequestAttribute<?>, HttpHandler>> httpRouterFactory
                spreadsheetEnvironmentContext,
                LOCALE_CONTEXT,
                SPREADSHEET_PROVIDER,
                PROVIDER_CONTEXT
            ),
            TERMINAL_CONTEXT
        );
    }

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public final String typeNameSuffix() {
        return "";
    }
}
