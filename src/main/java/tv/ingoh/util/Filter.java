package tv.ingoh.util;

public class Filter {
        static final char[] a = {
                '4',
                '@',
                'ª',
                'À',
                'Á',
                'Â',
                'Ã',
                'Ä',
                'Å',
                'à',
                'á',
                'â',
                'ã',
                'ä',
                'å',
                'Ā',
                'ā',
                'Ă',
                'ă',
                'Ą',
                'ą',
                'Ǻ',
                'ǻ',
                'Ά',
                'Α',
                'ά',
                'α',
                'А',
                'а',
                '∂',
        };
        static final char[] b = {
                'ß',
                'Ɓ',
                'ɓ',
                'ʙ',
                'Β',
                'β',
                'В',
                'Ь',
                'в',
                'ь',
                'Ѣ',
                'ѣ',
                'ℬ',
                '⒝',
                'Ⓑ',
                'ⓑ',
                '♭',
                'Ｂ',
                'ｂ',
        };
        static final char[] c = {
                '¢',
                '©',
                'Ç',
                'ç',
                'Ć',
                'ć',
                'Ĉ',
                'ĉ',
                'Ċ',
                'ċ',
                'Č',
                'č',
                'Ɔ',
                'ɔ',
                'ʗ',
                'С',
                'с',
                'Ҫ',
                'ҫ',
                '₡',
                'ℂ',
                '℃',
                'ℭ',
                'Ⅽ',
                'ⅽ',
                '⊂',
                '⊆',
                '⊏',
                '⒞',
                'Ⓒ',
                'ⓒ',
                '㏄',
                'Ｃ',
                'ｃ',
                '￠',
        };
        static final char[] d = {
                'Ď',
                'ď',
                'Đ',
                'đ',
                'Ɗ',
                'ɗ',
                'Ⅾ',
                'ⅾ',
                'ↁ',
                '⒟',
                'Ⓓ',
                'ⓓ',
                'Ｄ',
                'ｄ',
        };
        static final char[] e = {
                '3',
                'Æ',
                'È',
                'É',
                'Ê',
                'Ë',
                'æ',
                'è',
                'é',
                'ê',
                'ë',
                'Ē',
                'ē',
                'Ė',
                'ė',
                'Ę',
                'ę',
                'Ě',
                'ě',
                'Ə',
                'Ɛ',
                'Ɵ',
                'Ǽ',
                'ǽ',
                'ɘ',
                'ə',
                'ɛ',
                'ɜ',
                'ɵ',
                'ɶ',
                'Έ',
                'Ε',
                'έ',
                'ε',
                'Ё',
                'Є',
                'Е',
                'З',
                'Э',
                'е',
                'з',
                'э',
                'ё',
                'є',
                'Ҽ',
                'ҽ',
                'Ҿ',
                'ҿ',
                'Ӕ',
                'ӕ',
                'Ӗ',
                'ӗ',
                'Ә',
                'ә',
                'Ӛ',
                'ӛ',
                'Ӟ',
                'ӟ',
                'ℇ',
                '℮',
                'ℯ',
                'ℰ',
                '∃',
                '∈',
                '∉',
                '∊',
                '∋',
                '∌',
                '∑',
                '⒠',
                'Ⓔ',
                'ⓔ',
                'Ｅ',
                'ｅ',
        };
        static final char[] f = {
                'Ƒ',
                'ƒ',
                'ɟ',
                'Ғ',
                'ғ',
                '₣',
                '℉',
                'ℱ',
                'Ⅎ',
                '⒡',
                'Ⓕ',
                'ⓕ',
                'Ｆ',
                'ｆ',
        };
        static final char[] g = {
                'Ĝ',
                'ĝ',
                'Ğ',
                'ğ',
                'Ġ',
                'ġ',
                'Ģ',
                'ģ',
                'Ɠ',
                'Ǵ',
                'ǵ',
                'ɠ',
                'ɡ',
                'ɢ',
                'ʚ',
                'ʛ',
                'ℊ',
                '⒢',
                'Ⓖ',
                'ⓖ',
                'Ｇ',
                'ｇ',
        };
        static final char[] h = {
                'Ĥ',
                'ĥ',
                'Ħ',
                'ħ',
                'ɥ',
                'ɦ',
                'ɧ',
                'ʜ',
                'Ћ',
                'Н',
                'н',
                'ћ',
                'Ң',
                'ң',
                'Ҥ',
                'ҥ',
                'Ҷ',
                'ҷ',
                'Ҹ',
                'ҹ',
                'Һ',
                'һ',
                'Ӈ',
                'ӈ',
                'Ӌ',
                'ӌ',
                'Ӵ',
                'ӵ',
                'ℋ',
                'ℌ',
                'ℍ',
                'ℎ',
                'ℏ',
                '⒣',
                'Ⓗ',
                'ⓗ',
                'Ｈ',
                'ｈ',
        };
        static final char[] i = {
                'Ì',
                'Í',
                'Î',
                'Ï',
                'ì',
                'í',
                'î',
                'ï',
                'Ĩ',
                'ĩ',
                'Ī',
                'ī',
                'Ĭ',
                'ĭ',
                'Į',
                'į',
                'İ',
                'ı',
                'Ɨ',
                'Ǐ',
                'ǐ',
                'ɨ',
                'Ί',
                'Ϊ',
                'ί',
                'ϊ',
                'І',
                'Ї',
                'і',
                'ї',
                'ℐ',
                'ℑ',
                'ℒ',
                'ℓ',
                'Ⅰ',
                'Ⅱ',
                'Ⅲ',
                'ⅰ',
                'ⅱ',
                'ⅲ',
                '⒤',
                'Ⓘ',
                'ⓘ',
                'Ｉ',
                'ｉ',
                '1',
                'l',
                '|',
        };
        static final char[] j = {
                'Ĵ',
                'ĵ',
                'ʝ',
                'Ј',
                'ј',
                '⌡',
                '⒥',
                'Ⓙ',
                'ⓙ',
                'Ｊ',
                'ｊ',
        };
        static final char[] k = {
                'Ķ',
                'ķ',
                'ĸ',
                'ʞ',
                'Κ',
                'κ',
                'Ќ',
                'К',
                'к',
                'ќ',
                'Қ',
                'қ',
                'Ҝ',
                'ҝ',
                'Ҟ',
                'ҟ',
                'Ҡ',
                'ҡ',
                'K',
                '⒦',
                'Ⓚ',
                'ⓚ',
                'Ｋ',
                'ｋ',
        };
        static final char[] l = {
                '1',
                '£',
                'Ĺ',
                'ĺ',
                'Ļ',
                'ļ',
                'Ľ',
                'ľ',
                'Ŀ',
                'ŀ',
                'Ł',
                'ł',
                'ʟ',
                'Ι',
                'ι',
                'Ⅼ',
                'ⅼ',
                '⒧',
                'Ⓛ',
                'ⓛ',
                'Ｌ',
                'ｌ',
                '￡',
        };
        static final char[] m = {
                'Ɯ',
                'ɯ',
                'ɰ',
                'ɱ',
                'Μ',
                'μ',
                'М',
                'м',
                '₥',
                'ℳ',
                'Ⅿ',
                'ⅿ',
                '⒨',
                'Ⓜ',
                'ⓜ',
                '㎜',
                '㎟',
                '㎡',
                '㎣',
                '㎥',
                'Ｍ',
                'ｍ',
        };
        static final char[] n = {
                'Ñ',
                'ñ',
                'Ń',
                'ń',
                'Ņ',
                'ņ',
                'Ň',
                'ň',
                'ŉ',
                'Ŋ',
                'ŋ',
                'Ɲ',
                'ɲ',
                'ɳ',
                'ɴ',
                'Ή',
                'Η',
                'ή',
                'η',
                'И',
                'Й',
                'и',
                'й',
                'Ӣ',
                'ӣ',
                'Ӥ',
                'ӥ',
                '₦',
                'ℕ',
                '⒩',
                'Ⓝ',
                'ⓝ',
                'Ｎ',
                'ｎ',
        };
        static final char[] o = {
                '0',
                '¤',
                'º',
                'Ð',
                'Ò',
                'Ó',
                'Ô',
                'Õ',
                'Ö',
                'Ø',
                'ð',
                'ò',
                'ó',
                'ô',
                'õ',
                'ö',
                'ø',
                'Ō',
                'ō',
                'Ő',
                'ő',
                'Œ',
                'œ',
                'Ǒ',
                'ǒ',
                'Ǿ',
                'ǿ',
                'Ό',
                'Θ',
                'Ο',
                'Φ',
                'θ',
                'ο',
                'φ',
                'ό',
                'О',
                'о',
                'Ѻ',
                'ѻ',
                'Ӧ',
                'ӧ',
                '⒪',
                'Ⓞ',
                'ⓞ',
                '０',
                'Ｏ',
                'ｏ',
        };
        static final char[] p = {
                '¶',
                'Ρ',
                'ρ',
                'Р',
                'р',
                '℗',
                '℘',
                'ℙ',
                '⒫',
                'Ⓟ',
                'ⓟ',
                'Ｐ',
                'ｐ',
        };
        static final char[] q = {
                'ℚ',
                '⒬',
                'Ⓠ',
                'ⓠ',
                'Ｑ',
                'ｑ',
        };
        static final char[] r = {
                '®',
                'Ŕ',
                'ŕ',
                'Ŗ',
                'ŗ',
                'Ř',
                'ř',
                'ɹ',
                'ɺ',
                'ɻ',
                'ɼ',
                'ɽ',
                'ɾ',
                'ɿ',
                'ʀ',
                'ʁ',
                'Я',
                'я',
                'ℛ',
                'ℜ',
                'ℝ',
                '℞',
                '℟',
                '⒭',
                'Ⓡ',
                'ⓡ',
                'Ｒ',
                'ｒ',
        };
        static final char[] s = {
                '$',
                '§',
                'Ś',
                'ś',
                'Ŝ',
                'ŝ',
                'Ş',
                'ş',
                'Š',
                'š',
                'ʂ',
                'Ѕ',
                'ѕ',
                '⒮',
                'Ⓢ',
                'ⓢ',
                '＄',
                'Ｓ',
                'ｓ',
        };
        static final char[] t = {
                '7',
                'Ţ',
                'ţ',
                'Ť',
                'ť',
                'Ŧ',
                'ŧ',
                'Ʈ',
                'ʇ',
                'ʈ',
                'Т',
                'т',
                'Ҭ',
                'ҭ',
                '⒯',
                'Ⓣ',
                'ⓣ',
                'Ｔ',
                'ｔ',
        };
        static final char[] u = {
                '_',
                '|',
                'µ',
                'Ù',
                'Ú',
                'Û',
                'Ü',
                'ù',
                'ú',
                'û',
                'ü',
                'Ũ',
                'ũ',
                'Ū',
                'ū',
                'Ŭ',
                'ŭ',
                'Ů',
                'ů',
                'Ű',
                'ű',
                'Ų',
                'ų',
                'Ʊ',
                'Ʋ',
                'Ǔ',
                'ǔ',
                'Ǖ',
                'ǖ',
                'Ǘ',
                'ǘ',
                'Ǚ',
                'ǚ',
                'Ǜ',
                'ǜ',
                'ʉ',
                'ʊ',
                'ʋ',
                'Џ',
                'џ',
                '⋃',
                '⒰',
                'Ⓤ',
                'ⓤ',
                'Ｕ',
                'ｕ',
                'v',
        };
        static final char[] v = {
                'ʌ',
                'Ν',
                'ΰ',
                'ν',
                'Ѵ',
                'ѵ',
                'Ѷ',
                'ѷ',
                '℣',
                'Ⅴ',
                'ⅴ',
                '⒱',
                'Ⓥ',
                'ⓥ',
                'Ｖ',
                'ｖ',
        };
        static final char[] w = {
                'Ŵ',
                'ŵ',
                'ʍ',
                'Ώ',
                'Ω',
                'ω',
                'ώ',
                'Ѡ',
                'ѡ',
                'Ẁ',
                'ẁ',
                'Ẃ',
                'ẃ',
                'Ẅ',
                'ẅ',
                '₩',
                '⒲',
                'Ⓦ',
                'ⓦ',
                'Ｗ',
                'ｗ',
        };
        static final char[] x = {
                '×',
                'Χ',
                'χ',
                'Х',
                'х',
                'Ҳ',
                'ҳ',
                'Ⅹ',
                'ⅹ',
                '⒳',
                'Ⓧ',
                'ⓧ',
                '〷',
                'Ｘ',
                'ｘ',
        };
        static final char[] y = {
                '¥',
                'Ý',
                'ý',
                'ÿ',
                'Ŷ',
                'ŷ',
                'Ÿ',
                'ʎ',
                'ʏ',
                'Ύ',
                'Υ',
                'Ϋ',
                'υ',
                'ϋ',
                'ύ',
                'Ў',
                'У',
                'у',
                'ў',
                'Ү',
                'ү',
                'Ұ',
                'ұ',
                'Ӯ',
                'ӯ',
                'Ӱ',
                'ӱ',
                'Ӳ',
                'ӳ',
                'Ỳ',
                'ỳ',
                '⒴',
                'Ⓨ',
                'ⓨ',
                'Ｙ',
                'ｙ',
                '￥',
        };
        static final char[] z = {
                'Ź',
                'ź',
                'Ż',
                'ż',
                'Ž',
                'ž',
                'ℤ',
                '⒵',
                'Ⓩ',
                'ⓩ',
                'Ｚ',
                'ｚ',
        };

        public static final String[] containsBanned = {
                "fag",
                "faggot",
                "hentai",
                "molest",
                "nazi",
                "nigga",
                "nigger",
                "niglet",
                "paedo",
                "pedo",
                "porn",
                "porns",
                "rape",
                "rapist",
                "retard",
                "sex crime",
                "sex offender",
                "trannie",
                "tranny",
        };

        public static final String[] exactBanned = {
                "kill me",
                "kill yourself",
                "kill your self",
                "kill myself",
                "kill my self",
                "killing me",
                "killing yourself",
                "killing your self",
                "killing myself",
                "killing my self",
                "kms",
                "kys",
                "suicide"
        };

        public static boolean isBanned(String s) {
                for (String string : containsBanned) {
                        if (s.contains(string)) return true;
                }
                for (String string : exactBanned) {
                        if (s.matches("([^a-zA-Z]|^)*" + string + "([^a-zA-Z]|$)*")) return true;
                }
                return false;
        }
}
