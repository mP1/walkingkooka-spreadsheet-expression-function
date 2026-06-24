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
import walkingkooka.spreadsheet.expression.FakeSpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;

import java.util.Optional;

public final class SpreadsheetExpressionFunctionSpreadsheetMetadataLoadTest extends SpreadsheetExpressionFunctionSpreadsheetMetadataTestCase<SpreadsheetExpressionFunctionSpreadsheetMetadataLoad, SpreadsheetMetadata>
    implements SpreadsheetMetadataTesting {

    private final static SpreadsheetId SPREADSHEET_ID = SpreadsheetId.with(1);

    private final static SpreadsheetMetadata METADATA = METADATA_EN_AU.set(
        SpreadsheetMetadataPropertyName.SPREADSHEET_ID,
        SPREADSHEET_ID
    );

    @Test
    public void testApplyUnknownSpreadsheet() {
        this.applyAndCheck(
            Lists.of(
                SpreadsheetId.with(404)
            ),
            null
        );
    }

    @Test
    public void testApplySpreadsheetFound() {
        this.applyAndCheck(
            Lists.of(SPREADSHEET_ID),
            METADATA
        );
    }

    @Override
    public SpreadsheetExpressionFunctionSpreadsheetMetadataLoad createBiFunction() {
        return SpreadsheetExpressionFunctionSpreadsheetMetadataLoad.INSTANCE;
    }

    @Override
    public SpreadsheetExpressionEvaluationContext createContext() {
        return new FakeSpreadsheetExpressionEvaluationContext() {

            @Override
            public Optional<SpreadsheetMetadata> loadMetadata(final SpreadsheetId id) {
                return Optional.ofNullable(
                    SpreadsheetExpressionFunctionSpreadsheetMetadataLoadTest.SPREADSHEET_ID.equals(id) ?
                        METADATA :
                        null
                );
            }
        };
    }

    @Override
    public int minimumParameterCount() {
        return 1;
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createBiFunction(),
            "loadSpreadsheetMetadata"
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetExpressionFunctionSpreadsheetMetadataLoad> type() {
        return SpreadsheetExpressionFunctionSpreadsheetMetadataLoad.class;
    }
}
