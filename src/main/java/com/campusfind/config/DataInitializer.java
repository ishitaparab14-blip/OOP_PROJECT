package com.campusfind.config;

import com.campusfind.entity.Claim;
import com.campusfind.entity.Item;
import com.campusfind.entity.Notification;
import com.campusfind.entity.User;
import com.campusfind.enums.ClaimStatus;
import com.campusfind.enums.ItemCategory;
import com.campusfind.enums.ItemStatus;
import com.campusfind.enums.ItemType;
import com.campusfind.enums.Role;
import com.campusfind.repository.ClaimRepository;
import com.campusfind.repository.ItemRepository;
import com.campusfind.repository.NotificationRepository;
import com.campusfind.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@Profile("dev")
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ClaimRepository claimRepository;
    private final NotificationRepository notificationRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${campusfind.seed-demo-data:true}")
    private boolean seedDemoData;

    @Value("${campusfind.admin.name:System Administrator}")
    private String adminName;

    @Value("${campusfind.admin.email:admin@campusfind.edu}")
    private String adminEmail;

    @Value("${campusfind.admin.password:Admin@123Password}")
    private String adminPassword;

    public DataInitializer(UserRepository userRepository,
                           ItemRepository itemRepository,
                           ClaimRepository claimRepository,
                           NotificationRepository notificationRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.claimRepository = claimRepository;
        this.notificationRepository = notificationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (!seedDemoData) {
            return;
        }

        // Avoid duplicate seeding if admin already exists
        if (userRepository.existsByEmailIgnoreCase(adminEmail)) {
            return;
        }

        // 1. Seed Administrator
        User admin = new User(
                adminName,
                adminEmail.toLowerCase(),
                passwordEncoder.encode(adminPassword),
                Role.ROLE_ADMIN
        );
        userRepository.save(admin);

        // 2. Seed Fictional College Students
        User aarav = userRepository.save(new User(
                "Aarav Sharma",
                "aarav.student@campusfind.edu",
                passwordEncoder.encode("Password@123"),
                Role.ROLE_STUDENT
        ));

        User diya = userRepository.save(new User(
                "Diya Patel",
                "diya.student@campusfind.edu",
                passwordEncoder.encode("Password@123"),
                Role.ROLE_STUDENT
        ));

        User rohan = userRepository.save(new User(
                "Rohan Verma",
                "rohan.student@campusfind.edu",
                passwordEncoder.encode("Password@123"),
                Role.ROLE_STUDENT
        ));

        User sneha = userRepository.save(new User(
                "Sneha Nair",
                "sneha.student@campusfind.edu",
                passwordEncoder.encode("Password@123"),
                Role.ROLE_STUDENT
        ));

        // 3. Seed 11 Realistic Sample Listings (Lost, Found, Claim Pending, Returned, Resolved)
        Item item1 = itemRepository.save(new Item(
                "Apple AirPods Pro 2 in White Charging Case",
                "Lost my 2nd generation AirPods Pro inside the Central Library 2nd floor reading hall near desk 14. The case has a tiny blue star sticker on the back.",
                ItemCategory.ELECTRONICS,
                ItemType.LOST,
                "Central Library - 2nd Floor Study Cubicle",
                LocalDate.now().minusDays(2),
                "https://images.unsplash.com/photo-1600294037681-c80b4cb5b434?w=600&auto=format&fit=crop&q=80",
                aarav
        ));

        Item item2 = itemRepository.save(new Item(
                "White Wireless Earbuds in Matte Case",
                "Found wireless earbuds left behind on a corner table in the Library Quiet Study Hall around 5:30 PM. Turned over to lost-and-found desk.",
                ItemCategory.ELECTRONICS,
                ItemType.FOUND,
                "Central Library - Quiet Study Hall",
                LocalDate.now().minusDays(1),
                "https://images.unsplash.com/photo-1590658268037-6bf12165a8df?w=600&auto=format&fit=crop&q=80",
                diya
        ));

        Item item3 = itemRepository.save(new Item(
                "Milton Thermosteel 1000ml Blue Water Bottle",
                "Left my dark blue stainless steel insulated Milton bottle on workshop bench #4 during CAD lab session. Has minor scratch near base.",
                ItemCategory.OTHER,
                ItemType.LOST,
                "Mechanical Engineering Workshop Bench 4",
                LocalDate.now().minusDays(3),
                "https://images.unsplash.com/photo-1602143407151-7111542de6e8?w=600&auto=format&fit=crop&q=80",
                rohan
        ));

        Item item4 = itemRepository.save(new Item(
                "Blue Insulated Steel Water Flask",
                "Found an insulated cold water flask left on the cafeteria counter table near the juice stall during lunch break.",
                ItemCategory.OTHER,
                ItemType.FOUND,
                "Ground Floor Cafeteria Counter",
                LocalDate.now().minusDays(2),
                "https://images.unsplash.com/photo-1544816155-12df9643f363?w=600&auto=format&fit=crop&q=80",
                sneha
        ));

        Item item5 = itemRepository.save(new Item(
                "Black Samsonite Laptop Backpack with Laptop Charger",
                "Black backpack containing a Lenovo ThinkPad charger and class notes. Accompanying key fob attached to front zipper.",
                ItemCategory.BAGS_WALLETS,
                ItemType.LOST,
                "Computer Science Lab 3, 3rd Floor",
                LocalDate.now().minusDays(1),
                "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=600&auto=format&fit=crop&q=80",
                sneha
        ));

        Item item6 = itemRepository.save(new Item(
                "Automatic 3-Fold Compact Black Umbrella",
                "Found a black windproof foldable umbrella with wooden handle left under seat H-12 after the orientation ceremony.",
                ItemCategory.CLOTHING_ACCESSORIES,
                ItemType.FOUND,
                "Main Auditorium - Row H",
                LocalDate.now().minusDays(4),
                "https://images.unsplash.com/photo-1534353436294-0dbd4bdac845?w=600&auto=format&fit=crop&q=80",
                aarav
        ));

        Item item7 = itemRepository.save(new Item(
                "Royal Blue Student ID Card Lanyard & 2 Hostel Keys",
                "Found a blue college ribbon lanyard containing a student ID badge and two brass keys on a brass ring near court 2.",
                ItemCategory.KEYS_CARDS,
                ItemType.FOUND,
                "Sports Complex - Badminton Court 2",
                LocalDate.now().minusDays(2),
                "https://images.unsplash.com/photo-1589829545856-d10d557cf95f?w=600&auto=format&fit=crop&q=80",
                diya
        ));
        item7.setStatus(ItemStatus.CLAIM_PENDING);
        itemRepository.save(item7);

        Item item8 = itemRepository.save(new Item(
                "Honda Bike Key Set with Metal Superhero Keychain",
                "Lost a set of two keys with a dark grey Captain America shield charm between the east parking lot and entrance.",
                ItemCategory.KEYS_CARDS,
                ItemType.LOST,
                "East Parking Gate 2",
                LocalDate.now().minusDays(5),
                "https://images.unsplash.com/photo-1582139329536-e7284fece509?w=600&auto=format&fit=crop&q=80",
                rohan
        ));

        Item item9 = itemRepository.save(new Item(
                "Casio FX-991CW ClassWiz Scientific Calculator",
                "Found a scientific calculator in black slip-on cover left behind after the Engineering Math mid-term exam.",
                ItemCategory.ELECTRONICS,
                ItemType.FOUND,
                "Examination Hall B - Desk 42",
                LocalDate.now().minusDays(6),
                "https://images.unsplash.com/photo-1594980596870-8aa52a78d8cd?w=600&auto=format&fit=crop&q=80",
                sneha
        ));
        item9.setStatus(ItemStatus.RETURNED);
        itemRepository.save(item9);

        Item item10 = itemRepository.save(new Item(
                "Handwritten Data Structures & Algorithms Notebook",
                "Hardbound yellow ruled notebook with handwritten tree and graph algorithm diagrams. Successfully recovered from friend!",
                ItemCategory.BOOKS_STATIONERY,
                ItemType.LOST,
                "Student Activity Center Lounge",
                LocalDate.now().minusDays(7),
                "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=600&auto=format&fit=crop&q=80",
                diya
        ));
        item10.setStatus(ItemStatus.RESOLVED);
        itemRepository.save(item10);

        Item item11 = itemRepository.save(new Item(
                "Original 65W Fast Charger & Type-C Braided Cable",
                "Found a white 65W GaN wall charger plugged into the wall outlet next to workstation 8.",
                ItemCategory.ELECTRONICS,
                ItemType.FOUND,
                "Innovation Lab Room 204",
                LocalDate.now().minusDays(1),
                "https://images.unsplash.com/photo-1583863788434-e58a36330cf0?w=600&auto=format&fit=crop&q=80",
                aarav
        ));

        // 4. Seed Sample Claim (Pending review on item 7)
        Claim sampleClaim = new Claim(
                item7,
                rohan,
                "I lost my hostel room keys and college lanyard while playing badminton on Wednesday evening.",
                "Student ID has name Rohan Verma with Roll No 23CS084. Hostel key has a tiny notch cut into the head of the secondary key."
        );
        claimRepository.save(sampleClaim);

        // 5. Seed Initial Welcome Notifications
        notificationRepository.save(new Notification(
                rohan,
                "Your ownership claim for '" + item7.getTitle() + "' was submitted and is awaiting administrator verification.",
                item7.getId(),
                sampleClaim.getId()
        ));

        notificationRepository.save(new Notification(
                aarav,
                "Welcome to CampusFind! Your registered email is " + aarav.getEmail(),
                null,
                null
        ));
    }
}
