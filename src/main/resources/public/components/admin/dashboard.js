export default {
  template: `
<div class="container mt-5">
    <h2 class="text-center">Admin Dashboard</h2>
    <div class="row">
        <div class="col-md-6">
            <button @click="$root.changeView('admin_claims')" class="btn btn-secondary w-100 mb-3">Manage Claims</button>
        </div>
        <div class="col-md-6">
            <button @click="$root.changeView('admin_users')" class="btn btn-secondary w-100">Manage Users</button>
        </div>
    </div>
</div>
  `
};

