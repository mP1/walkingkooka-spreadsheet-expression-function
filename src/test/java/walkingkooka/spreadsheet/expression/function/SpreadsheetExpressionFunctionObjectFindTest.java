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
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.engine.SpreadsheetMetadataMode;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContexts;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.store.FakeSpreadsheetMetadataStore;
import walkingkooka.spreadsheet.meta.store.SpreadsheetMetadataStore;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReferenceLoaders;
import walkingkooka.spreadsheet.store.repo.FakeSpreadsheetStoreRepository;
import walkingkooka.spreadsheet.value.SpreadsheetErrorKind;

import java.util.Optional;

public final class SpreadsheetExpressionFunctionObjectFindTest extends SpreadsheetExpressionFunctionObjectTestCase<SpreadsheetExpressionFunctionObjectFind> {

    @Test
    public void testFound() {
        this.applyAndCheck2(
            Lists.of(
                "abc",
                "before abc"
            ),
            EXPRESSION_NUMBER_KIND.create(1 + "before ".length())
        );
    }

    @Test
    public void testNotFound() {
        this.applyAndCheck2(
            Lists.of(
                "NOT",
                "before abc"
            ),
            SpreadsheetErrorKind.VALUE.setMessage("\"NOT\" not found in \"before abc\"")
        );
    }

    @Override
    public SpreadsheetExpressionFunctionObjectFind createBiFunction() {
        return SpreadsheetExpressionFunctionObjectFind.INSTANCE;
    }

    @Override
    public int minimumParameterCount() {
        return 2;
    }

    @Override
    public SpreadsheetExpressionEvaluationContext createContext() {
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
            SpreadsheetContexts.basic(
                (id) -> {
                    if (spreadsheetId.equals(id)) {
                        return new FakeSpreadsheetStoreRepository() {
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
                        };
                    }
                    throw new IllegalArgumentException("Unknown SpreadsheetId: " + id);
                },
                SPREADSHEET_PROVIDER,
                (c) -> {
                    throw new UnsupportedOperationException();
                }, // Function<SpreadsheetContext, SpreadsheetEngineContext> spreadsheetEngineContextFactory
                (c) -> {
                    throw new UnsupportedOperationException();
                }, // Function<SpreadsheetEngineContext, Router<HttpRequestAttribute<?>, HttpHandler>> httpRouterFactory
                SPREADSHEET_ENVIRONMENT_CONTEXT.cloneEnvironment()
                    .setSpreadsheetId(spreadsheetId),
                LOCALE_CONTEXT,
                PROVIDER_CONTEXT,
                TERMINAL_SERVER_CONTEXT
            ),
            TERMINAL_CONTEXT
        );
    }

    @Override
    public Class<SpreadsheetExpressionFunctionObjectFind> type() {
        return SpreadsheetExpressionFunctionObjectFind.class;
    }
}
