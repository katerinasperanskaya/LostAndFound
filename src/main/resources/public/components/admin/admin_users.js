export default {
  template: `
    <div class="container py-5">

      <div class="d-flex align-items-center justify-content-between mb-4">
        <h1 class="text-dark fw-normal">Manage Users</h1>
        <button class="btn btn-primary btn-sm shadow-sm" data-bs-toggle="modal" data-bs-target="#userModal">
          <i class="bi bi-person-plus me-2"></i> Add User/Admin
        </button>
      </div>

      <!-- Tabs for Users and Admins -->
      <ul class="nav nav-tabs mb-4">
        <li class="nav-item">
          <a class="nav-link" :class="{ active: activeTab === 'users' }" href="#" @click.prevent="switchTab('users')">Users</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" :class="{ active: activeTab === 'admins' }" href="#" @click.prevent="switchTab('admins')">Admins</a>
        </li>
      </ul>

      <!-- Data Table -->
      <div class="card border-0 shadow-sm rounded-4 overflow-hidden">
        <div class="card-body p-5">
          <table :id="activeTab + 'Table'" class="table-hover align-middle table table-striped table-bordered w-100" style="min-width: 800px;">
            <thead class="table-light">
              <tr>
                <th class="ps-4">ID</th>
                <th>Name</th>
                <th>Email</th>
                <th>Phone</th>
                <th style="width: 100px;"></th>
              </tr>
            </thead>
            <tbody>
              <!-- Data populated via DataTables -->
            </tbody>
          </table>
        </div>
      </div>

      <!-- Add/Edit Modal -->
      <div class="modal fade" id="userModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
          <div class="modal-content rounded-4 shadow">
            <div class="modal-header border-bottom-0 pb-0">
              <h5 class="modal-title fw-bold" id="modalTitle">{{ editMode ? 'Edit ' + (isUser ? 'User' : 'Admin') : 'Add New ' + (isUser ? 'User' : 'Admin') }}</h5>
              <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body pt-0">
              <form id="userForm" class="needs-validation" novalidate>
                <input type="hidden" id="userId">
                <div class="mb-3">
                  <label for="name" class="form-label fw-semibold">Name</label>
                  <div class="input-group">
                    <span class="input-group-text border-end-0 bg-transparent">
                      <i class="bi bi-person"></i>
                    </span>
                    <input type="text" class="form-control border-start-0" id="name" required placeholder="John Doe">
                  </div>
                  <div class="invalid-feedback">Please enter a valid name</div>
                </div>
                <div class="mb-3">
                  <label for="email" class="form-label fw-semibold">Email</label>
                  <div class="input-group">
                    <span class="input-group-text border-end-0 bg-transparent">
                      <i class="bi bi-envelope"></i>
                    </span>
                    <input type="email" class="form-control border-start-0" id="email" required placeholder="example@example.com">
                  </div>
                  <div class="invalid-feedback">Please enter a valid email</div>
                </div>
                <div class="mb-3">
                  <label for="phone" class="form-label fw-semibold">Phone</label>
                  <div class="input-group">
                    <span class="input-group-text border-end-0 bg-transparent">
                      <i class="bi bi-phone"></i>
                    </span>
                    <input type="text" class="form-control border-start-0" id="phone" required placeholder="9876543210">
                  </div>
                  <div class="invalid-feedback">Please enter a valid phone number</div>
                </div>
                <div class="mb-3" id="passwordGroup">
                  <label for="password" class="form-label fw-semibold">Password</label>
                  <div class="input-group">
                    <span class="input-group-text border-end-0 bg-transparent">
                      <i class="bi bi-lock"></i>
                    </span>
                    <input type="password" class="form-control border-start-0" id="password" placeholder="••••••••">
                  </div>
                  <div class="form-text text-muted small">
                    Minimum 6 characters with at least one number
                  </div>
                </div>
              </form>
            </div>
            <div class="modal-footer border-top-0 pt-0">
              <button type="button" class="btn btn-link text-secondary" data-bs-dismiss="modal">Cancel</button>
              <button type="button" class="btn btn-primary rounded-pill px-4" id="saveUser">
                <span class="spinner-border spinner-border-sm me-2 d-none" role="status" aria-hidden="true"></span>
                Save
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Delete Confirmation Modal -->
      <div class="modal fade" id="deleteModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
          <div class="modal-content rounded-4 shadow">
            <div class="modal-body text-center p-4">
              <i class="bi bi-exclamation-triangle display-3 text-danger mb-3"></i>
              <h5 class="fw-bold mb-3">Confirm Deletion</h5>
              <p class="mb-4">Are you sure you want to permanently delete this {{ isUser ? 'user' : 'admin' }}?</p>
              <button type="button" class="btn btn-outline-secondary me-3" data-bs-dismiss="modal">Cancel</button>
              <button type="button" class="btn btn-danger rounded-pill px-4" id="confirmDelete">
                <span class="spinner-border spinner-border-sm me-2 d-none" role="status" aria-hidden="true"></span>
                Delete
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Toast Notification -->
      <div class="position-fixed top-0 end-0 p-3" style="z-index: 1050;">
        <div id="alertToast" class="toast align-items-center" role="alert">
          <div class="toast-body d-flex align-items-center">
            <div class="toast-icon me-3">
              <i class="bi fs-4" :class="toastType === 'success' ? 'bi-check-circle text-success' : 'bi-exclamation-triangle text-danger'"></i>
            </div>
            <div>
              <strong class="me-auto" id="toastTitle">Success</strong>
              <div class="text-muted small" id="toastMessage">User added successfully</div>
            </div>
            <button type="button" class="btn-close ms-auto" data-bs-dismiss="toast"></button>
          </div>
        </div>
      </div>
    </div>
  `,
  data() {
    return {
      activeTab: 'users', // Current tab (users or admins)
      dataTable: null,
      userToDelete: null,
      editMode: false,
      isUser: true, // True for users, false for admins
      toastType: '',
      toastMessage: ''
    };
  },
  mounted() {
    this.initDataTable();
    this.initializeEventListeners();
  },
  methods: {
    switchTab(tab) {
      this.activeTab = tab;
      this.isUser = tab === 'users';
      this.dataTable.destroy();
      this.initDataTable();
    },
    initDataTable() {
      const endpoint = this.isUser ? 'http://localhost:9091/api/users' : 'http://localhost:9091/api/admins';
      this.dataTable = $(`#${this.activeTab}Table`).DataTable({
        ajax: {
          url: endpoint,
          dataSrc: ''
        },
        columns: [
          { data: 'id' },
          { data: 'name' },
          { data: 'email' },
          { data: 'phone' },
          {
            data: null,
            orderable: false,
            className: 'text-center',
            render: (data, type, row) => `
              <button class="btn btn-primary btn-sm edit-user me-2 px-3 mb-2" data-id="${row.id}">
                <i class="bi bi-pencil"></i> Edit
              </button>
              <button class="btn btn-primary btn-sm delete-user me-2" data-id="${row.id}">
                <i class="bi bi-trash"></i> Delete
              </button>
            `
          }
        ]
      });
    },
    initializeEventListeners() {
      const userModal = new bootstrap.Modal(document.getElementById('userModal'));
      const deleteModal = new bootstrap.Modal(document.getElementById('deleteModal'));
      const toast = new bootstrap.Toast(document.getElementById('alertToast'));

      // Edit user/admin button click
      $('#usersTable, #adminsTable').on('click', '.edit-user', (e) => {
        const id = $(e.currentTarget).data('id');
        const rowData = this.dataTable.row($(e.currentTarget).closest('tr')).data();
        this.showUserModal(rowData);
      });

      // Delete user/admin button click
      $('#usersTable, #adminsTable').on('click', '.delete-user', (e) => {
        this.userToDelete = $(e.currentTarget).data('id');
        deleteModal.show();
      });

      // Save user/admin button click
      $('#saveUser').on('click', () => this.saveUser());

      // Confirm delete
      $('#confirmDelete').on('click', () => this.deleteUser());
    },
    showUserModal(data = null) {
      this.editMode = !!data;
      if (data) {
        document.getElementById('userId').value = data.id;
        document.getElementById('name').value = data.name;
        document.getElementById('email').value = data.email;
        document.getElementById('phone').value = data.phone;
        document.getElementById('passwordGroup').style.display = 'none';
      } else {
        document.getElementById('userForm').reset();
        document.getElementById('passwordGroup').style.display = 'block';
      }
      new bootstrap.Modal(document.getElementById('userModal')).show();
    },
    async saveUser() {
      const form = document.getElementById('userForm');
      if (!form.checkValidity()) {
        form.reportValidity();
        return;
      }

      const userData = {
        name: document.getElementById('name').value,
        email: document.getElementById('email').value,
        phone: document.getElementById('phone').value,
        password: document.getElementById('password').value || undefined
      };

      const id = document.getElementById('userId').value;
      const endpoint = this.editMode
        ? `${this.isUser ? 'http://localhost:9091/api/users' : 'http://localhost:9091/api/admins'}/${id}`
        : `${this.isUser ? 'http://localhost:9091/api/users' : 'http://localhost:9091/api/admins'}`;
      const method = this.editMode ? 'PUT' : 'POST';

      try {
        const response = await fetch(endpoint, {
          method,
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(userData)
        });

        if (!response.ok) throw new Error('Failed to save');

        this.showToast('Success', `${this.isUser ? 'User' : 'Admin'} ${this.editMode ? 'updated' : 'created'} successfully!`, 'success');
        this.dataTable.ajax.reload();
        $('#userModal').modal('hide');
      } catch (error) {
        this.showToast('Error', error.message || 'Operation failed', 'error');
      }
    },
    async deleteUser() {
      const endpoint = `${this.isUser ? 'http://localhost:9091/api/users' : 'http://localhost:9091/api/admins'}/${this.userToDelete}`;

      try {
        const response = await fetch(endpoint, {
          method: 'DELETE',
          headers: { 'Content-Type': 'application/json' }
        });

        if (!response.ok) throw new Error('Failed to delete');

        this.showToast('Success', `${this.isUser ? 'User' : 'Admin'} deleted successfully!`, 'success');
        this.dataTable.ajax.reload();
        $('#deleteModal').modal('hide');
      } catch (error) {
        this.showToast('Error', error.message || 'Failed to delete', 'error');
      }
    },
    showToast(title, message, type) {
      document.getElementById('toastTitle').textContent = title;
      document.getElementById('toastMessage').textContent = message;
      const toast = document.getElementById('alertToast');
      toast.className = `toast ${type === 'error' ? 'bg-danger text-white' : 'bg-success text-white'}`;
      new bootstrap.Toast(toast).show();
    }
  }
};