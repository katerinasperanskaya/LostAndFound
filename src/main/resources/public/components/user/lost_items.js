export default {
  template: `
  <div class="container mt-5">
     <h1>Lost Items</h1>

     <!-- Category Filter Dropdown -->
     <div class="mb-3">
        <label for="category" class="form-label">Filter by Category</label>
        <select v-model="selectedCategory" class="form-select" id="category" @change="filterItems">
            <option value="">All Categories</option>
            <option v-for="category in categories" :key="category" :value="category">
                {{ category }}
            </option>
        </select>
     </div>

     <!-- Lost Items Table -->
     <div v-if="filteredItems.length">
        <table class="table table-striped table-bordered mt-3">
            <thead class="table-primary">
                <tr>
                    <th>Title</th>
                    <th>Description</th>
                    <th>Category</th>
                    <th>Location</th>
                    <th>Date Lost</th>
                    <th>Status</th>
                    <th>Image</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="item in filteredItems" :key="item.id">
                    <td>{{ item.title }}</td>
                    <td>{{ item.description }}</td>
                    <td>{{ item.category }}</td>
                    <td>{{ item.locationLost }}</td>
                    <td>{{ formatDate(item.dateLost) }}</td>
                    <td>
                        <span :class="{'badge bg-success': item.status === 'FOUND', 'badge bg-warning': item.status === 'OPEN'}">
                            {{ item.status }}
                        </span>
                    </td>
                    <td>
                        <img :src="item.imageUrl" alt="Lost Item Image" class="img-thumbnail" width="80">
                    </td>
                </tr>
            </tbody>
        </table>
     </div>

     <!-- No Items Found Message -->
     <p v-else class="text-muted text-center">No lost items found.</p>
  </div>
  `,
  data() {
    return {
      selectedCategory: "",
      categories: [],
      lostItems: [],
      filteredItems: []
    };
  },
  async created() {
    try {
      const response = await fetch("http://localhost:9091/api/lost-items");
      this.lostItems = await response.json();

      // Extract unique categories from lost items
      this.categories = [...new Set(this.lostItems.map(item => item.category))];

      // Show all lost items by default
      this.filteredItems = this.lostItems;
    } catch (error) {
      console.error("Error fetching lost items:", error);
    }
  },
  methods: {
    filterItems() {
      // If no category is selected, show all lost items
      this.filteredItems = this.selectedCategory
        ? this.lostItems.filter(item => item.category === this.selectedCategory)
        : this.lostItems;
    },
    formatDate(dateString) {
      return new Date(dateString).toLocaleDateString();
    }
  }
};
