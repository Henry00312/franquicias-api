// Script de inicialización de MongoDB
// Este script se ejecuta automáticamente cuando MongoDB inicia

db = db.getSiblingDB('franquicias');

// Crear colecciones con validación de esquema
db.createCollection('franquicias', {
  validator: {
    $jsonSchema: {
      bsonType: 'object',
      required: ['nombre', 'fechaCreacion'],
      properties: {
        _id: { bsonType: 'objectId' },
        nombre: { bsonType: 'string' },
        sucursales: { 
          bsonType: 'array',
          items: { bsonType: 'objectId' }
        },
        fechaCreacion: { bsonType: 'date' },
        fechaActualizacion: { bsonType: 'date' }
      }
    }
  }
});

db.createCollection('sucursales', {
  validator: {
    $jsonSchema: {
      bsonType: 'object',
      required: ['nombre', 'franquiciaId', 'fechaCreacion'],
      properties: {
        _id: { bsonType: 'objectId' },
        nombre: { bsonType: 'string' },
        franquiciaId: { bsonType: 'objectId' },
        productos: { 
          bsonType: 'array',
          items: { bsonType: 'objectId' }
        },
        fechaCreacion: { bsonType: 'date' },
        fechaActualizacion: { bsonType: 'date' }
      }
    }
  }
});

db.createCollection('productos', {
  validator: {
    $jsonSchema: {
      bsonType: 'object',
      required: ['nombre', 'sucursalId', 'stock', 'fechaCreacion'],
      properties: {
        _id: { bsonType: 'objectId' },
        nombre: { bsonType: 'string' },
        sucursalId: { bsonType: 'objectId' },
        stock: { bsonType: 'int' },
        fechaCreacion: { bsonType: 'date' },
        fechaActualizacion: { bsonType: 'date' }
      }
    }
  }
});

// Crear índices para mejorar performance
db.franquicias.createIndex({ nombre: 1 });
db.sucursales.createIndex({ franquiciaId: 1 });
db.sucursales.createIndex({ nombre: 1 });
db.productos.createIndex({ sucursalId: 1 });
db.productos.createIndex({ sucursalId: 1, stock: -1 });
db.productos.createIndex({ nombre: 1 });

print('Base de datos franquicias inicializada correctamente');
