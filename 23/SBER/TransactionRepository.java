import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionRepository {

    public List<Transaction> getAllTransactions() {
        // Mock transactions, replace with actual database access
        return List.of(
            new Transaction("1", 100.0, new Date(), "NEW"),
            new Transaction("2", -50.0, new Date(), "NEW"),
            new Transaction("3", 200.0, new Date(), "NEW")
        );
    }
}
