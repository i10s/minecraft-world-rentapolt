#!/usr/bin/env python3
"""
Generate improved placeholder textures for Rentapolt entities and items.
Uses PIL to create distinctive, colorful 64x64 entity textures and 16x16 item/block textures.
"""

from PIL import Image, ImageDraw
import os

# Entity texture configurations (64x64)
ENTITIES = {
    'lion': {'base': (218, 165, 32), 'accent': (255, 215, 0), 'pattern': 'mane'},
    'elephant': {'base': (128, 128, 128), 'accent': (192, 192, 192), 'pattern': 'trunk'},
    'phoenix': {'base': (255, 69, 0), 'accent': (255, 215, 0), 'pattern': 'flames'},
    'griffin': {'base': (139, 69, 19), 'accent': (255, 215, 0), 'pattern': 'wings'},
    'mutant_creeper': {'base': (0, 128, 0), 'accent': (0, 255, 0), 'pattern': 'mutant'},
    'mutant_zombie': {'base': (34, 139, 34), 'accent': (144, 238, 144), 'pattern': 'mutant'},
    'fire_golem': {'base': (139, 0, 0), 'accent': (255, 69, 0), 'pattern': 'lava'},
    'plasma_beast': {'base': (138, 43, 226), 'accent': (255, 0, 255), 'pattern': 'energy'},
    'shadow_serpent': {'base': (25, 25, 112), 'accent': (75, 0, 130), 'pattern': 'serpent'}
}

# Block texture configurations (16x16)
BLOCKS = {
    'energy_block': {'base': (0, 255, 255), 'accent': (255, 255, 0), 'glow': True},
    'teleporter_block': {'base': (138, 43, 226), 'accent': (75, 0, 130), 'glow': True},
    'city_glow_block': {'base': (0, 191, 255), 'accent': (255, 255, 255), 'glow': True},
    'explosive_block': {'base': (220, 20, 60), 'accent': (255, 255, 0), 'glow': False}
}

# Item texture configurations (16x16)
ITEMS = {
    'fire_sword': {'base': (255, 69, 0), 'accent': (255, 215, 0)},
    'lightning_bow': {'base': (135, 206, 250), 'accent': (255, 255, 0)},
    'teleportation_dagger': {'base': (138, 43, 226), 'accent': (255, 0, 255)},
    'explosive_hammer': {'base': (105, 105, 105), 'accent': (255, 0, 0)},
    'plasma_rifle': {'base': (0, 255, 255), 'accent': (138, 43, 226)},
    'shadow_scythe': {'base': (25, 25, 112), 'accent': (128, 0, 128)}
}

def create_entity_texture(name, config):
    """Create a 64x64 entity texture with pattern."""
    img = Image.new('RGBA', (64, 64), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)
    
    base = config['base']
    accent = config['accent']
    pattern = config.get('pattern', 'simple')
    
    # Body (main area)
    draw.rectangle([16, 20, 48, 52], fill=base + (255,))
    
    # Head
    draw.rectangle([24, 8, 40, 20], fill=base + (255,))
    
    # Legs
    for x in [20, 28, 36, 44]:
        draw.rectangle([x, 52, x+3, 64], fill=base + (255,))
    
    # Pattern-specific details
    if pattern == 'mane':
        # Lion's mane
        draw.ellipse([20, 6, 44, 22], fill=accent + (255,))
        draw.rectangle([24, 8, 40, 20], fill=base + (255,))  # Face over mane
    elif pattern == 'flames':
        # Phoenix flames
        for i in range(0, 64, 8):
            draw.ellipse([i, 4, i+8, 12], fill=accent + (200,))
    elif pattern == 'wings':
        # Griffin wings
        draw.polygon([(16, 24), (8, 20), (8, 32), (16, 40)], fill=accent + (255,))
        draw.polygon([(48, 24), (56, 20), (56, 32), (48, 40)], fill=accent + (255,))
    elif pattern == 'mutant':
        # Mutant spots/mutations
        for i in range(5):
            x, y = 20 + i*6, 24 + (i % 3)*8
            draw.ellipse([x, y, x+4, y+4], fill=accent + (255,))
    elif pattern == 'energy':
        # Energy core
        draw.ellipse([28, 32, 36, 40], fill=accent + (255,))
    elif pattern == 'serpent':
        # Serpent body (elongated)
        for i in range(8):
            y = 16 + i*6
            draw.ellipse([26, y, 38, y+8], fill=accent + (200,))
    
    # Eyes
    draw.rectangle([28, 12, 30, 14], fill=(255, 255, 255, 255))
    draw.rectangle([34, 12, 36, 14], fill=(255, 255, 255, 255))
    
    return img

def create_block_texture(name, config):
    """Create a 16x16 block texture."""
    img = Image.new('RGBA', (16, 16), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)
    
    base = config['base']
    accent = config['accent']
    glow = config.get('glow', False)
    
    # Base fill
    draw.rectangle([0, 0, 15, 15], fill=base + (255,))
    
    # Border
    draw.rectangle([0, 0, 15, 15], outline=accent + (255,), width=1)
    
    # Special patterns
    if 'energy' in name:
        # Animated energy pattern (central glow)
        draw.ellipse([4, 4, 11, 11], fill=accent + (255,))
        draw.rectangle([7, 2, 8, 13], fill=accent + (255,))
        draw.rectangle([2, 7, 13, 8], fill=accent + (255,))
    elif 'teleporter' in name:
        # Portal swirl
        draw.arc([2, 2, 13, 13], 0, 270, fill=accent + (255,), width=2)
        draw.ellipse([6, 6, 9, 9], fill=accent + (255,))
    elif 'glow' in name:
        # Neon grid
        for i in range(0, 16, 4):
            draw.line([(i, 0), (i, 15)], fill=accent + (200,), width=1)
            draw.line([(0, i), (15, i)], fill=accent + (200,), width=1)
    elif 'explosive' in name:
        # Warning pattern
        draw.polygon([(8, 2), (14, 8), (8, 14), (2, 8)], fill=accent + (255,))
        draw.text((6, 5), "!", fill=(0, 0, 0, 255))
    
    return img

def create_item_texture(name, config):
    """Create a 16x16 item texture."""
    img = Image.new('RGBA', (16, 16), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)
    
    base = config['base']
    accent = config['accent']
    
    # Weapon-specific shapes
    if 'sword' in name or 'scythe' in name or 'dagger' in name:
        # Blade
        draw.polygon([(8, 2), (9, 2), (9, 12), (8, 12)], fill=accent + (255,))
        # Handle
        draw.rectangle([7, 12, 9, 15], fill=base + (255,))
        # Guard
        draw.rectangle([5, 11, 11, 12], fill=base + (255,))
    elif 'bow' in name:
        # Bow shape
        draw.arc([2, 2, 10, 14], 270, 90, fill=base + (255,), width=2)
        draw.line([(6, 2), (6, 14)], fill=accent + (255,), width=1)
    elif 'hammer' in name:
        # Hammer head
        draw.rectangle([4, 3, 12, 7], fill=accent + (255,))
        # Handle
        draw.rectangle([7, 7, 9, 15], fill=base + (255,))
    elif 'rifle' in name:
        # Rifle barrel
        draw.rectangle([3, 6, 13, 8], fill=base + (255,))
        # Stock
        draw.rectangle([2, 8, 6, 11], fill=base + (255,))
        # Energy cell
        draw.rectangle([7, 5, 9, 9], fill=accent + (255,))
    
    return img

def main():
    base_path = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
    textures_path = os.path.join(base_path, 'src', 'main', 'resources', 'assets', 'rentapolt', 'textures')
    
    print("Generating improved placeholder textures...")
    
    # Generate entity textures
    entity_path = os.path.join(textures_path, 'entity')
    for name, config in ENTITIES.items():
        img = create_entity_texture(name, config)
        output_path = os.path.join(entity_path, f'{name}.png')
        img.save(output_path)
        print(f"  ✓ Created {name}.png (64x64)")
    
    # Generate block textures
    block_path = os.path.join(textures_path, 'block')
    for name, config in BLOCKS.items():
        img = create_block_texture(name, config)
        output_path = os.path.join(block_path, f'{name}.png')
        img.save(output_path)
        print(f"  ✓ Created {name}.png (16x16)")
    
    # Generate item textures
    item_path = os.path.join(textures_path, 'item')
    for name, config in ITEMS.items():
        img = create_item_texture(name, config)
        output_path = os.path.join(item_path, f'{name}.png')
        img.save(output_path)
        print(f"  ✓ Created {name}.png (16x16)")
    
    print(f"\n✓ Generated {len(ENTITIES)} entity textures")
    print(f"✓ Generated {len(BLOCKS)} block textures")
    print(f"✓ Generated {len(ITEMS)} item textures")
    print("\nNote: These are improved placeholders. Create custom pixel art for production!")

if __name__ == '__main__':
    main()
