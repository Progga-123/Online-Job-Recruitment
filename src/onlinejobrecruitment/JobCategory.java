

package onlinejobrecruitment;





import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobCategory {
    private int categoryId;
    private String categoryName;
    private String description;
    
    public JobCategory() {}
    
    public JobCategory(int categoryId, String categoryName, String description) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.description = description;
    }
    
    
    public static List<JobCategory> getAllCategories() {
        List<JobCategory> categories = new ArrayList<>();
        String sql = "SELECT category_id, name, detail FROM jobcategory ORDER BY name";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                JobCategory category = new JobCategory();
                category.setCategoryId(rs.getInt("category_id"));
                category.setCategoryName(rs.getString("name"));
                category.setDescription(rs.getString("detail"));
                categories.add(category);
            }
        } catch (SQLException e) {
            System.out.println("Failed to fetch categories: " + e.getMessage());
        }
        
        return categories;
    }
    
    
    public static void displayCategories() {
        List<JobCategory> categories = getAllCategories();
        
        if (categories.isEmpty()) {
            System.out.println("No categories available.");
            return;
        }
        
        System.out.println("\n=== Available Job Categories ===");
        for (JobCategory category : categories) {
            System.out.println(category.getCategoryId() + ". " + category.getCategoryName());
            if (category.getDescription() != null && !category.getDescription().isEmpty()) {
                System.out.println("   Description: " + category.getDescription());
            }
        }
        System.out.println("================================\n");
    }
    
   
    public static boolean isValidCategory(int categoryId) {
        String sql = "SELECT COUNT(*) FROM jobcategory WHERE category_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, categoryId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error validating category: " + e.getMessage());
        }
        
        return false;
    }
    
    
    public int getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        return "Category ID: " + categoryId + ", Name: " + categoryName + 
               (description != null ? ", Description: " + description : "");
    }
}