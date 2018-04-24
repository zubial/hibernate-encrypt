package net.zubial.hibernate.encrypt.utils;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class StringUtils {

    private static final Logger LOGGER = LogManager.getLogger(StringUtils.class);

    private static final String RETURN_REGEX = "[\\r\\n]";
    private static final String EXCLUSION_REGEX = "[^A-Za-z0-9._()\\[\\]-]";
    private static final String TOKEN_REGEX = "[^A-Za-z0-9\\-]";
    private static final String URL_REGEX = "^(https?|ftp):[-a-zA-Z0-9+&@#\\/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#\\/%=~_|]";

    private StringUtils() {
        // pvt const
    }

    public static Boolean parseBoolean(final String value) {
        return isNotBlank(value)
                && ("1".equals(value)
                || "Y".equalsIgnoreCase(value)
                || "YES".equalsIgnoreCase(value)
                || "TRUE".equalsIgnoreCase(value)
                || "OK".equalsIgnoreCase(value)
        );

    }

    /**
     * Create a string with the given words.
     *
     * @param words the list of word
     * @return the string
     */
    public static String createString(final String... words) {
        final StringBuilder builder = new StringBuilder();
        for (final String word : words) {
            builder.append(word);
        }
        return builder.toString();
    }

    public static String concat(final String... words) {
        final StringBuilder builder = new StringBuilder();
        if (words != null) {
            for (final String word : words) {
                if (isNotBlank(word)) {
                    builder.append(word);
                    builder.append(" ");
                }
            }
        }
        return trim(builder.toString());
    }

    /**
     * Check if a string is blank or null.
     *
     * @param value the value to check
     * @return true if the string is not blank, false otherwise.
     */
    public static Boolean isNotBlank(final String value) {
        return value != null && !"".equals(value);
    }

    /**
     * Check if a string is blank or null.
     *
     * @param value the value to check
     * @return true if the string is blank, false otherwise.
     */
    public static Boolean isBlank(final String value) {
        return value == null || "".equals(value);
    }

    public static String join(final List<String> items) {
        return join(items, "|");
    }

    public static String join(final List<String> items, final String separator) {
        final StringBuilder builder = new StringBuilder();
        if (items != null) {
            for (final String item : items) {
                builder.append(item);
                builder.append(separator);
            }
        }
        return builder.toString();
    }

    public static String trim(final String value) {
        if (value != null) {
            return value.trim();
        }
        return "";
    }

    public static String substring(final String string, final int endIndex) {
        try {
            if (string == null) {
                return null;
            }
            if (string.length() > endIndex) {
                return string.substring(0, endIndex);
            } else {
                return string;
            }
        } catch (final StringIndexOutOfBoundsException e) {
            LOGGER.debug("substring", e);
            return string;
        }
    }

    public static String substringLength(final String string, final int lenght) {
        try {
            if (string == null) {
                return null;
            }
            if (string.length() > lenght) {
                return string.substring(0, lenght - 1);
            }
            return string;
        } catch (final StringIndexOutOfBoundsException e) {
            LOGGER.debug("substringLength", e);
            return string;
        }
    }

    public static String noReturnString(final String input) {
        return input.trim().replaceAll(RETURN_REGEX, "");
    }

    public static String useHtmlReturnString(final String input) {
        return input.trim().replaceAll(RETURN_REGEX, "<br/>");
    }

    public static String[] splitReturnLine(final String input) {
        return input.trim().split(RETURN_REGEX);
    }

    public static String cleanUpString(final String input) {
        return input.trim().replaceAll("[âäãà]", "a").replaceAll("[éèëê]", "e").replaceAll("[îï]", "i").replaceAll("[öôõ]", "o").replaceAll("[üû]", "u").replaceAll("ÿ", "y").replaceAll("ç", "c").replaceAll("ñ", "n")
                .replaceAll(EXCLUSION_REGEX, "");
    }

    public static String sanitizeToken(final String input) {
        if (input != null) {
            return input.replaceAll(TOKEN_REGEX, "");
        }
        return input;
    }

    public static Boolean validateUrl(final String input) {
        if (input != null) {
            return input.matches(URL_REGEX);
        }
        return false;
    }

    public static String formatFirstLetterMaj(final String string) {
        if (string != null) {
            if (string.length() == 1) {
                return string.toUpperCase();
            } else if (string.length() > 1) {
                return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
            }
        }
        return string;
    }

    public static String remplaceParams(final String templateContent, final Map<String, String> params) {
        String templateResult = templateContent;
        if (templateResult != null && !templateResult.isEmpty() && params != null) {
            for (final Entry<String, String> param : params.entrySet()) {
                if (param != null && param.getKey() != null && param.getValue() != null) {
                    templateResult = templateResult.replaceAll("\\{" + param.getKey() + "\\}", param.getValue());
                }
            }
        }
        return templateResult;
    }

    public static String sanitizeSqlString(String value) {
        return sanitizeSqlString(value, true);
    }

    public static String sanitizeSqlString(String value, boolean escapeDoubleQuotes) {
        if (value == null) {
            return null;
        }

        StringBuilder sBuilder = new StringBuilder(value.length() * 11 / 10);

        int stringLength = value.length();

        for (int i = 0; i < stringLength; ++i) {
            char c = value.charAt(i);

            switch (c) {
                case 0: /* Must be escaped for 'mysql' */
                    sBuilder.append('\\');
                    sBuilder.append('0');

                    break;

                case '\n': /* Must be escaped for logs */
                    sBuilder.append('\\');
                    sBuilder.append('n');

                    break;

                case '\r':
                    sBuilder.append('\\');
                    sBuilder.append('r');

                    break;

                case '\\':
                    sBuilder.append('\\');
                    sBuilder.append('\\');

                    break;

                case '\'':
                    sBuilder.append('\\');
                    sBuilder.append('\'');

                    break;

                case '"': /* Better safe than sorry */
                    if (escapeDoubleQuotes) {
                        sBuilder.append('\\');
                    }
                    sBuilder.append('"');
                    break;
                case '\032': /* This gives problems on Win32 */
                    sBuilder.append('\\');
                    sBuilder.append('Z');

                    break;

                case '\u00a5':
                case '\u20a9':
                    // escape characters interpreted as backslash by mysql
                    // fall through

                default:
                    sBuilder.append(c);
            }
        }

        return sBuilder.toString();
    }

    public static String replace(String text, String search, String replace) {
        if (search == null) {
            return text;
        }

        if (text != null) {
            if (replace != null) {
                return text.replace(search, replace);
            } else {
                return text.replace(search, "");
            }
        }

        return "";
    }
}
