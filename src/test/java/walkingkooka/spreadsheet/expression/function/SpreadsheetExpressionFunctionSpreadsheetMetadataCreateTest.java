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
import walkingkooka.net.email.EmailAddress;
import walkingkooka.spreadsheet.expression.FakeSpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;

import java.util.Locale;
import java.util.Optional;

public final class SpreadsheetExpressionFunctionSpreadsheetMetadataCreateTest extends SpreadsheetExpressionFunctionSpreadsheetMetadataTestCase<SpreadsheetExpressionFunctionSpreadsheetMetadataCreate, SpreadsheetMetadata>
    implements SpreadsheetMetadataTesting {

    private final static Locale LOCALE = Locale.forLanguageTag("en-NZ");

    private final static EmailAddress USER = EmailAddress.parse("SpreadsheetExpressionFunctionSpreadsheetMetadataCreateTest@example.com");

    private final static SpreadsheetMetadata METADATA = METADATA_EN_AU.set(
        SpreadsheetMetadataPropertyName.SPREADSHEET_ID,
        SpreadsheetId.with(1)
    );

    @Test
    public void testApplyLocaleMissing() {
        this.applyAndCheck(
            Lists.empty(),
            METADATA.set(
                SpreadsheetMetadataPropertyName.AUDIT_INFO,
                METADATA.getOrFail(
                        SpreadsheetMetadataPropertyName.AUDIT_INFO
                    ).setCreatedBy(USER)
                    .setModifiedBy(USER)
            )
        );
    }

    @Test
    public void testApplyLocalePresent() {
        this.applyAndCheck(
            Lists.of(LOCALE),
            METADATA.set(
                SpreadsheetMetadataPropertyName.LOCALE,
                LOCALE
            ).set(
                SpreadsheetMetadataPropertyName.AUDIT_INFO,
                METADATA.getOrFail(
                        SpreadsheetMetadataPropertyName.AUDIT_INFO
                    ).setCreatedBy(USER)
                    .setModifiedBy(USER)
            )
        );
    }

    @Override
    public SpreadsheetExpressionFunctionSpreadsheetMetadataCreate createBiFunction() {
        return SpreadsheetExpressionFunctionSpreadsheetMetadataCreate.INSTANCE;
    }

    @Override
    public SpreadsheetExpressionEvaluationContext createContext() {
        return new FakeSpreadsheetExpressionEvaluationContext() {
            @Override
            public SpreadsheetMetadata createMetadata(final EmailAddress user,
                                                      final Optional<Locale> locale) {
                return locale.map(
                        (final Locale l) -> METADATA.set(
                            SpreadsheetMetadataPropertyName.LOCALE,
                            l
                        )
                    ).orElse(METADATA)
                    .set(
                        SpreadsheetMetadataPropertyName.AUDIT_INFO,
                        METADATA.getOrFail(
                                SpreadsheetMetadataPropertyName.AUDIT_INFO
                            ).setCreatedBy(SpreadsheetExpressionFunctionSpreadsheetMetadataCreateTest.USER)
                            .setModifiedBy(SpreadsheetExpressionFunctionSpreadsheetMetadataCreateTest.USER)
                    );
            }

            @Override
            public Optional<EmailAddress> user() {
                return Optional.of(
                    SpreadsheetExpressionFunctionSpreadsheetMetadataCreateTest.USER
                );
            }
        };
    }

    @Override
    public int minimumParameterCount() {
        return 0;
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createBiFunction(),
            "createSpreadsheetMetadata"
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetExpressionFunctionSpreadsheetMetadataCreate> type() {
        return SpreadsheetExpressionFunctionSpreadsheetMetadataCreate.class;
    }
}
