package net.sf.jabref.model.util;

public class RemoveLatexCommands {

    public String format(String field) {

        StringBuilder sb = new StringBuilder("");
        StringBuilder currentCommand = null;
        char c;
        boolean escaped = false;
        boolean incommand = false;
        int i;
        for (i = 0; i < field.length(); i++) {
            c = field.charAt(i);
            if (escaped && (c == '\\')) {
                sb.append('\\');
                escaped = false;
            } else if (c == '\\') {
                escaped = true;
                incommand = true;
                currentCommand = new StringBuilder();
            } else if (!incommand && ((c == '{') || (c == '}'))) {
                // Swallow the brace.
            } else if (Character.isLetter(c) ||
                    ModelStringUtil.SPECIAL_COMMAND_CHARS.contains(String.valueOf(c))) {
                escaped = false;
                if (incommand) {
                    currentCommand.append(c);
                    if ((currentCommand.length() == 1)
                            && ModelStringUtil.SPECIAL_COMMAND_CHARS.contains(currentCommand.toString())) {
                        // This indicates that we are in a command of the type \^o or \~{n}
                        incommand = false;
                        escaped = false;

                    }
                } else {
                    sb.append(c);
                }
            } else if (Character.isLetter(c)) {
                escaped = false;
                if (incommand) {
                    // We are in a command, and should not keep the letter.
                    currentCommand.append(c);
                } else {
                    sb.append(c);
                }
            } else {
                if (!incommand || (!Character.isWhitespace(c) && (c != '{'))) {
                    sb.append(c);
                } else {
                    if (c != '{') {
                        sb.append(c);
                    }
                }
                incommand = false;
                escaped = false;
            }
        }

        return sb.toString();
    }
}