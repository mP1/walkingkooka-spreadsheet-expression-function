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
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.SpreadsheetContexts;
import walkingkooka.spreadsheet.engine.SpreadsheetMetadataMode;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContexts;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.store.FakeSpreadsheetMetadataStore;
import walkingkooka.spreadsheet.meta.store.SpreadsheetMetadataStore;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReferenceLoaders;
import walkingkooka.spreadsheet.store.repo.FakeSpreadsheetStoreRepository;
import walkingkooka.tree.text.TextNode;

import java.util.Locale;
import java.util.Optional;

public final class SpreadsheetExpressionFunctionTextNodeFormatValueTest extends SpreadsheetExpressionFunctionTextNodeTestCase<SpreadsheetExpressionFunctionTextNodeFormatValue> {

    @Test
    public void testApplyWithTextFormatter() {
        this.applyAndCheck2(
            Lists.of(
                SpreadsheetFormatterSelector.DEFAULT_TEXT_FORMAT.setValueText("@@"),
                "Hello"
            ),
            TextNode.text("HelloHello")
        );
    }

    // test related factories...........................................................................................

    @Override
    public SpreadsheetExpressionFunctionTextNodeFormatValue createBiFunction() {
        return SpreadsheetExpressionFunctionTextNodeFormatValue.INSTANCE;
    }

    @Override
    public SpreadsheetExpressionEvaluationContext createContext() {
        final Locale locale = Locale.ENGLISH;

        final SpreadsheetId spreadsheetId = SpreadsheetId.parse("1234");

        final SpreadsheetMetadata metadata = METADATA_EN_AU.set(
            SpreadsheetMetadataPropertyName.SPREADSHEET_ID,
            spreadsheetId
        );

        return SpreadsheetExpressionEvaluationContexts.spreadsheetContext(
            SpreadsheetMetadataMode.FORMULA,
            SpreadsheetMetadata.NO_CELL,
            SpreadsheetExpressionReferenceLoaders.fake(),
            SPREADSHEET_LABEL_NAME_RESOLVER,
            SpreadsheetContexts.fixedSpreadsheetId(
                new FakeSpreadsheetStoreRepository() {
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
                SPREADSHEET_ENVIRONMENT_CONTEXT.cloneEnvironment()
                    .setSpreadsheetId(spreadsheetId),
                LOCALE_CONTEXT,
                SPREADSHEET_PROVIDER,
                PROVIDER_CONTEXT
            ),
            TERMINAL_CONTEXT
        );
    }

    @Override
    public int minimumParameterCount() {
        return 2;
    }

    @Override
    public Class<SpreadsheetExpressionFunctionTextNodeFormatValue> type() {
        return SpreadsheetExpressionFunctionTextNodeFormatValue.class;
    }
}
