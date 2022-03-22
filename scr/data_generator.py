### Generate fake names, addresses, IDs, titles, etc. Save in a text file
from faker import Faker


fake = Faker('en_US')
f_len = 10

# full names
with open('fake_data/full_name.txt', 'w+') as f:
    for i in range(f_len):
        f.write(fake.name() + '\n')
        #print(full_name)

# first names
with open('fake_data/first_name.txt', 'w+') as f:
    for i in range(f_len):
        f.write(fake.first_name() + '\n')

# last names
with open('fake_data/last_name.txt', 'w+') as f:
    for i in range(f_len):
        f.write(fake.last_name() + '\n')

# dates
with open('fake_data/dates.txt', 'w+') as f:
    for i in range(f_len):
        f.write(fake.date() + '\n')

# publisher companies
with open('fake_data/publishers.txt', 'w+') as f:
    for i in range(f_len):
        f.write(fake.company() + '\n')

# isbn13
isbns = set(fake.unique.isbn13() for i in range(f_len))
if len(isbns) == f_len:
    with open('fake_data/isbn.txt', 'w+') as f:
        for i in range(f_len):
            f.write(list(isbns)[i] + '\n')
else: print('File too short')

# random strings (article texts)
with open('fake_data/article_text.txt', 'w+') as f:
    for i in range(f_len):
        f.write(fake.paragraph(nb_sentences=5) + '\n')

# Publisher ID
with open('fake_data/publisher_ID.txt', 'w+') as f:
    for i in range(f_len):
        f.write(str(i+1) + '\n')

# Publication ID
with open('fake_data/publication_ID.txt', 'w+') as f:
    for i in range(f_len):
        f.write(str(i+1) + '\n')


