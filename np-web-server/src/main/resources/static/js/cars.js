'use strict';

const productTable = document.getElementById('product-table');
const prevPageBtn = document.getElementById('prev-page');
const nextPageBtn = document.getElementById('next-page');
const currentPageEl = document.getElementById('current-page');
const addCarBtn = document.getElementById('add-car-btn');
const addCarModal = document.getElementById('add-car-modal');
const addCarForm = document.getElementById('add-car-form');
const closeBtn = document.getElementsByClassName('close')[0];
const deleteModal = document.getElementById('delete-modal');
const cancelDeleteBtn = document.getElementById('cancel-delete');
const confirmDeleteBtn = document.getElementById('confirm-delete');
const editCarModal = document.getElementById('edit-car-modal');
const editCarForm = document.getElementById('edit-car-form');
const editModalCloseBtn = editCarModal.querySelector('.close');

let currentPage = 1;
const pageSize = 5;
let totalPages = 1;

function updatePaginationButtons() {
    // Disable/enable previous button based on current page
    prevPageBtn.disabled = currentPage <= 1;
    prevPageBtn.classList.toggle('button-disabled', currentPage <= 1);

    // Disable/enable next button based on total pages
    nextPageBtn.disabled = currentPage >= totalPages;
    nextPageBtn.classList.toggle('button-disabled', currentPage >= totalPages);
}

function loadProducts(page = 1) {
    // Don't allow invalid page numbers
    if (page < 1 || (totalPages > 0 && page > totalPages)) {
        return;
    }

    fetch(`/interauto/all?page=${page - 1}&size=${pageSize}`)
        .then(response => response.json())
        .then(data => {
            currentPage = page;
            // Calculate total pages from the response
            totalPages = Math.ceil(data.data.totalElements / pageSize);

            currentPageEl.textContent = currentPage;
            displayProducts(data.data.content);

            // Update pagination buttons state
            updatePaginationButtons();
        })
        .catch(error => {
            console.error('Error fetching products:', error);
        });
}

// Modify the click event listeners to include validation
prevPageBtn.addEventListener('click', () => {
    if (currentPage > 1) {
        loadProducts(currentPage - 1);
    }
});

nextPageBtn.addEventListener('click', () => {
    if (currentPage < totalPages) {
        loadProducts(currentPage + 1);
    }
});

// Add some basic CSS for disabled buttons
const style = document.createElement('style');
style.textContent = `
    .button-disabled {
        opacity: 0.5;
        cursor: not-allowed;
    }
`;
document.head.appendChild(style);

// Modal functions
addCarBtn.onclick = function() {
    addCarModal.style.display = "block";
}

closeBtn.onclick = function() {
    addCarModal.style.display = "none";
}

window.onclick = function(event) {
    if (event.target === addCarModal) {
        addCarModal.style.display = "none";
    }
}

function displayProducts(products) {
    productTable.getElementsByTagName('tbody')[0].innerHTML = '';
    products.forEach(product => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${product.name}</td>
            <td>${product.price}</td>
            <td>${product.year}</td>
            <td>${product.mileage}</td>
            <td>${product.gearbox}</td>
            <td>${product.fuel}</td>
            <td>
                <button class="button" onclick="editProduct(${product.id})">Edit</button>
                <button class="button" onclick="deleteProduct(${product.id})">Delete</button>
            </td>
        `;
        productTable.getElementsByTagName('tbody')[0].appendChild(row);
    });
}

function addProduct(event) {
    event.preventDefault();
    const formData = {
        name: document.getElementById('name').value,
        price: parseFloat(document.getElementById('price').value),
        year: parseInt(document.getElementById('year').value),
        mileage: document.getElementById('mileage').value,
        gearbox: document.getElementById('gearbox').value,
        fuel: document.getElementById('fuel').value
    };

    fetch('/interauto/car/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
        .then(response => response.json())
        .then(data => {
            loadProducts(currentPage);
            addCarForm.reset();
            addCarModal.style.display = "none";
        })
        .catch(error => {
            console.error('Error adding car:', error);
        });
}

loadProducts();
addCarForm.addEventListener('submit', addProduct);
prevPageBtn.addEventListener('click', () => loadProducts(currentPage - 1));
nextPageBtn.addEventListener('click', () => loadProducts(currentPage + 1));

let carToDelete = null;

// Edit modal close functionality
editModalCloseBtn.onclick = function() {
    editCarModal.style.display = "none";
}

// When user clicks outside either modal, close it
window.onclick = function(event) {
    if (event.target === addCarModal) {
        addCarModal.style.display = "none";
    } else if (event.target === deleteModal) {
        deleteModal.style.display = "none";
    } else if (event.target === editCarModal) {
        editCarModal.style.display = "none";
    }
}

function editProduct(id) {
    // First, fetch the car details
    fetch(`/interauto/car/${id}`)
        .then(response => response.json())
        .then(data => {
            const car = data.data;
            // Populate the edit form with car data
            document.getElementById('edit-car-id').value = car.id;
            document.getElementById('edit-name').value = car.name;
            document.getElementById('edit-price').value = car.price;
            document.getElementById('edit-year').value = car.year;
            document.getElementById('edit-mileage').value = car.mileage;
            document.getElementById('edit-gearbox').value = car.gearbox;
            document.getElementById('edit-fuel').value = car.fuel;

            // Show the edit modal
            editCarModal.style.display = "block";
        })
        .catch(error => {
            console.error('Error fetching car details:', error);
        });
}

// Handle edit form submission
editCarForm.addEventListener('submit', function(event) {
    event.preventDefault();
    const carId = document.getElementById('edit-car-id').value;

    const formData = {
        name: document.getElementById('edit-name').value,
        price: parseFloat(document.getElementById('edit-price').value),
        year: parseInt(document.getElementById('edit-year').value),
        mileage: document.getElementById('edit-mileage').value,
        gearbox: document.getElementById('edit-gearbox').value,
        fuel: document.getElementById('edit-fuel').value
    };

    fetch(`/interauto/car/${carId}/update`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
        .then(response => response.json())
        .then(data => {
            loadProducts(currentPage);
            editCarModal.style.display = "none";
            editCarForm.reset();
        })
        .catch(error => {
            console.error('Error updating car:', error);
        });
});

function deleteProduct(id) {
    carToDelete = id;
    deleteModal.style.display = "block";
}

// Cancel delete
cancelDeleteBtn.onclick = function() {
    deleteModal.style.display = "none";
    carToDelete = null;
}

// Confirm delete
confirmDeleteBtn.onclick = function() {
    if (carToDelete) {
        fetch(`/interauto/car/${carToDelete}/delete`, {
            method: 'DELETE'
        })
            .then(response => response.json())
            .then(data => {
                loadProducts(currentPage);
                deleteModal.style.display = "none";
                carToDelete = null;
            })
            .catch(error => {
                console.error('Error deleting car:', error);
            });
    }
}