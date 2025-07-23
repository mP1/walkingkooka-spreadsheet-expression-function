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

import walkingkooka.CanBeEmpty;
import walkingkooka.Value;
import walkingkooka.predicate.Predicates;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.HasCaseSensitivity;
import walkingkooka.text.HasText;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Contains a text match pattern and is also a {@link Predicate}.
 */
public final class TextMatch implements Value<String>,
    HasText,
    Predicate<CharSequence>,
    HasCaseSensitivity,
    CanBeEmpty {

    public static TextMatch parse(final String text) {
        Objects.requireNonNull(text, "text");

        return new TextMatch(text);
    }

    // @VisibleForTesting
    TextMatch(final String text) {
        this.text = text;
        this.predicate = Predicates.globPatterns(
            text,
            CASE_SENSITIVITY
        );
    }

    // value............................................................................................................

    @Override
    public String value() {
        return this.text;
    }

    private final String text;

    // HasText..........................................................................................................

    @Override
    public String text() {
        return this.value();
    }

    // HasCasSensitive..................................................................................................

    @Override
    public CaseSensitivity caseSensitivity() {
        return CASE_SENSITIVITY;
    }

    private final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.INSENSITIVE;

    // CanBeEmpty.......................................................................................................

    /**
     * A {@link TextMatch} is empty if it is only whitespace and has no actual glob pattern(s).
     */
    @Override
    public boolean isEmpty() {
        return this.text.trim()
            .isEmpty();
    }

    // Predicate........................................................................................................

    @Override
    public boolean test(final CharSequence text) {
        return this.predicate.test(text);
    }

    private final Predicate<CharSequence> predicate;

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return this.text.hashCode();
    }

    public boolean equals(final Object other) {
        return this == other ||
            other instanceof TextMatch && this.equals0((TextMatch) other);
    }

    private boolean equals0(final TextMatch other) {
        return CASE_SENSITIVITY.equals(text, other.text);
    }

    @Override
    public String toString() {
        return this.text;
    }
}
