package chess;
import java.util.ArrayList;
import java.util.Collection;

public class ValidMoves {

    public ValidMoves() {}
    public static Collection<ChessPosition> kingMoves(ChessBoard board, ChessPosition position){
        Collection<ChessPosition> valid = new ArrayList<>();
        int row = position.getRow();
        int column = position.getColumn();
        if (row >= 2){
            ChessPosition newPosition = new ChessPosition(row - 1, column);
            if (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != board.getPiece(position).getTeamColor()){
                valid.add(newPosition);
            }
            if (column < 8){
                ChessPosition newPosition3 = new ChessPosition(row - 1, column + 1);
                if (board.getPiece(newPosition3) == null || board.getPiece(newPosition3).getTeamColor() != board.getPiece(position).getTeamColor()){
                    valid.add(newPosition3);
                }
            }
            if (column >= 2){
                ChessPosition newPosition2 = new ChessPosition(row - 1, column - 1);
                if (board.getPiece(newPosition2) == null || board.getPiece(newPosition2).getTeamColor() != board.getPiece(position).getTeamColor()){
                    valid.add(newPosition2);
                }
            }
        }
        if (column >= 2){
            ChessPosition newPosition = new ChessPosition(row, column - 1);
            if (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != board.getPiece(position).getTeamColor()){
                valid.add(newPosition);
            }
        }
        if (row < 8){
            ChessPosition newPosition = new ChessPosition(row + 1, column);
            if (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != board.getPiece(position).getTeamColor()){
                valid.add(newPosition);
            }
            if (column > 1){
                ChessPosition newPosition3 = new ChessPosition(row + 1, column - 1);
                if (board.getPiece(newPosition3) == null || board.getPiece(newPosition3).getTeamColor() != board.getPiece(position).getTeamColor()){
                    valid.add(newPosition3);
                }
            }

            if (column < 8){
                ChessPosition newPosition2 = new ChessPosition(row + 1, column + 1);
                if (board.getPiece(newPosition2) == null || board.getPiece(newPosition2).getTeamColor() != board.getPiece(position).getTeamColor()){
                    valid.add(newPosition2);
                }
            }
        }
        if (column < 8){
            ChessPosition newPosition = new ChessPosition(row, column + 1);
            if (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != board.getPiece(position).getTeamColor()){
                valid.add(newPosition);
            }
        }
        return valid;
    }

    public static Collection<ChessPosition> queenMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessPosition> valid = new ArrayList<>();
        Collection<ChessPosition> queenMoves = new ArrayList<>();
        valid = bishopMoves(board, position);
        queenMoves = rookMoves(board, position);
        valid.addAll(queenMoves);
        return valid;
    }
    public static Collection<ChessPosition> bishopMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessPosition> valid = new ArrayList<>();
        int row = position.getRow();
        int column = position.getColumn();
        while (row < 8 && column < 8){
            ChessPosition newPosition = new ChessPosition(row + 1, column + 1);
            if (board.getPiece(newPosition) == null){
                valid.add(newPosition);
            }
            else if (board.getPiece(newPosition).getTeamColor() == board.getPiece(position).getTeamColor()){
                break;
            }
            else {
                valid.add(newPosition);
                break;
            }
            row += 1;
            column += 1;
        }
        row = position.getRow();
        column = position.getColumn();
        while (row >= 2 && column < 8){
            ChessPosition newPosition = new ChessPosition(row - 1, column + 1);
            if (board.getPiece(newPosition) == null){
                valid.add(newPosition);
            }
            else if (board.getPiece(newPosition).getTeamColor() == board.getPiece(position).getTeamColor()){
                break;
            }
            else {
                valid.add(newPosition);
                break;
            }
            row -= 1;
            column += 1;
        }
        row = position.getRow();
        column = position.getColumn();
        while (row >= 2 && column >= 2){
            ChessPosition newPosition = new ChessPosition(row - 1, column - 1);
            if (board.getPiece(newPosition) == null){
                valid.add(newPosition);
            }
            else if (board.getPiece(newPosition).getTeamColor() == board.getPiece(position).getTeamColor()){
                break;
            }
            else {
                valid.add(newPosition);
                break;
            }
            row -= 1;
            column -= 1;
        }
        row = position.getRow();
        column = position.getColumn();
        while (row < 8 && column >= 2){
            ChessPosition newPosition = new ChessPosition(row + 1, column - 1);
            if (board.getPiece(newPosition) == null){
                valid.add(newPosition);
            }
            else if (board.getPiece(newPosition).getTeamColor() == board.getPiece(position).getTeamColor()){
                break;
            }
            else {
                valid.add(newPosition);
                break;
            }
            row += 1;
            column -= 1;
        }
        return valid;
    }
    public static Collection<ChessPosition> knightMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessPosition> valid = new ArrayList<>();
        int row = position.getRow();
        int column = position.getColumn();

        if (row >= 3){
            if(column >= 2){
                ChessPosition newPosition = new ChessPosition(row - 2, column - 1);
                if (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != board.getPiece(position).getTeamColor()){
                    valid.add(newPosition);
                }
            }
            if (column < 8) {
                ChessPosition newPosition = new ChessPosition(row - 2, column + 1);
                if (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != board.getPiece(position).getTeamColor()){
                    valid.add(newPosition);
                }
            }
        }
        if (row >= 2){
            if(column >= 3){
                ChessPosition newPosition = new ChessPosition(row - 1, column - 2);
                if (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != board.getPiece(position).getTeamColor()){
                    valid.add(newPosition);
                }
            }
            if (column < 7) {
                ChessPosition newPosition = new ChessPosition(row - 1, column + 2);
                if (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != board.getPiece(position).getTeamColor()){
                    valid.add(newPosition);
                }
            }
        }
        if (row < 8){
            if (column >= 3){
                ChessPosition newPosition = new ChessPosition(row + 1, column - 2);
                if (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != board.getPiece(position).getTeamColor()){
                    valid.add(newPosition);
                }
            }
            if (column < 7) {
                ChessPosition newPosition = new ChessPosition(row + 1, column + 2);
                if (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != board.getPiece(position).getTeamColor()){
                    valid.add(newPosition);
                }
            }
        }
        if (row < 7){
            if(column >= 2){
                ChessPosition newPosition = new ChessPosition(row + 2, column - 1);
                if (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != board.getPiece(position).getTeamColor()){
                    valid.add(newPosition);
                }
            }
            if (column < 8) {
                ChessPosition newPosition = new ChessPosition(row + 2, column + 1);
                if (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != board.getPiece(position).getTeamColor()){
                    valid.add(newPosition);
                }
            }
        }
        return valid;
    }
    public static Collection<ChessPosition> pawnMoves(ChessBoard board, ChessPosition position, boolean isWhite) {
        Collection<ChessPosition> valid = new ArrayList<>();
        int row = position.getRow();
        int column = position.getColumn();
        if (isWhite){
            if (row < 8) {
                ChessPosition newPosition = new ChessPosition(row + 1, column);
                boolean yes = false;
                if (board.getPiece(newPosition) == null){
                    valid.add(newPosition);
                    yes = true;
                }
                if (column < 8) {
                    ChessPosition newPosition2 = new ChessPosition(row + 1, column + 1);
                    if (board.getPiece(newPosition2) != null &&
                            board.getPiece(newPosition2).getTeamColor() != board.getPiece(position).getTeamColor()){
                        valid.add(newPosition2);
                    }
                }
                if (column > 1) {
                    ChessPosition newPosition3 = new ChessPosition(row + 1, column - 1);
                    if (board.getPiece(newPosition3) != null &&
                            board.getPiece(newPosition3).getTeamColor() != board.getPiece(position).getTeamColor()){
                        valid.add(newPosition3);
                    }
                }
                if (row == 2) {
                    ChessPosition newPosition2 = new ChessPosition(row + 2, column);
                    if (board.getPiece(newPosition2) == null && yes){
                        valid.add(newPosition2);
                    }
                }
            }
        }
        else {
            if (row > 1) {
                ChessPosition newPosition = new ChessPosition(row - 1, column);
                boolean yes = false;
                if (board.getPiece(newPosition) == null){
                    valid.add(newPosition);
                    yes = true;
                }
                if (column < 8) {
                    ChessPosition newPosition2 = new ChessPosition(row - 1, column + 1);
                    if (board.getPiece(newPosition2) != null &&
                            board.getPiece(newPosition2).getTeamColor() != board.getPiece(position).getTeamColor()){
                        valid.add(newPosition2);
                    }
                }
                if (column > 1) {
                    ChessPosition newPosition3 = new ChessPosition(row - 1, column - 1);
                    if (board.getPiece(newPosition3) != null &&
                            board.getPiece(newPosition3).getTeamColor() != board.getPiece(position).getTeamColor()){
                        valid.add(newPosition3);
                    }
                }
                if (row == 7) {
                    ChessPosition newPosition2 = new ChessPosition(row - 2, column);
                    if (board.getPiece(newPosition2) == null && yes){
                        valid.add(newPosition2);
                    }
                }
            }
        }
        return valid;
    }

    public static Collection<ChessPosition> rookMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessPosition> valid = new ArrayList<>();
        int row = position.getRow();
        int column = position.getColumn();
        for (int i = row + 1; i < 9; i++){
            ChessPosition newPosition = new ChessPosition(i, column);
            if (board.getPiece(newPosition) != null && board.getPiece(newPosition).getTeamColor() != board.getPiece(position).getTeamColor()){
                valid.add(newPosition);
                break;
            }
            else if (board.getPiece(newPosition) == null){
                valid.add(newPosition);
            }
            else {
                break;
            }
        }
        for (int i = row - 1; i > 0; i--){
            ChessPosition newPosition1 = new ChessPosition(i, column);
            if (board.getPiece(newPosition1) != null && board.getPiece(newPosition1).getTeamColor() != board.getPiece(position).getTeamColor()){
                valid.add(newPosition1);
                break;
            }
            else if (board.getPiece(newPosition1) == null){
                valid.add(newPosition1);
            }
            else {
                break;
            }
        }
        for (int i = column - 1; i > 0; i--){
            ChessPosition newPosition2 = new ChessPosition(row, i);
            if (board.getPiece(newPosition2) != null && board.getPiece(newPosition2).getTeamColor() != board.getPiece(position).getTeamColor()){
                valid.add(newPosition2);
                break;
            }
            else if (board.getPiece(newPosition2) == null){
                valid.add(newPosition2);
            }
            else {
                break;
            }
        }
        for (int i = column + 1; i < 9; i++){
            ChessPosition newPosition3 = new ChessPosition(row, i);
            if (board.getPiece(newPosition3) != null && board.getPiece(newPosition3).getTeamColor() != board.getPiece(position).getTeamColor()){
                valid.add(newPosition3);
                break;
            }
            else if (board.getPiece(newPosition3) == null){
                valid.add(newPosition3);
            }
            else {
                break;
            }
        }
        return valid;
    }

    public static Collection<ChessPosition> returnValid(ChessBoard board, ChessPiece.PieceType pieceType, ChessPosition position, boolean isWhite) {
        Collection<ChessPosition> positions = new ArrayList<>();
        if (pieceType == ChessPiece.PieceType.KING) {
            positions = kingMoves(board, position);
        }
        else if (pieceType == ChessPiece.PieceType.QUEEN) {
            positions = queenMoves(board, position);
        }
        else if (pieceType == ChessPiece.PieceType.BISHOP) {
            positions = bishopMoves(board, position);
        }
        else if (pieceType == ChessPiece.PieceType.KNIGHT) {
            positions = knightMoves(board, position);
        }
        else if (pieceType == ChessPiece.PieceType.PAWN) {
            positions = pawnMoves(board, position, isWhite);
        }
        else if (pieceType == ChessPiece.PieceType.ROOK) {
            positions = rookMoves(board, position);
        }
        return positions;
    }


}
